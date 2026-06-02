package bean;

import entity.Author;
import entity.Book;
import entity.Category;
import facadeLocal.BookFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class ReadingAssistantBean implements Serializable {
    private static final int MAX_MESSAGES = 18;

    private String question;
    private String answer = "Merhaba, ben Rafine Asistan. Önce birkaç seçenekten birini seçebilir ya da kendi isteğini yazabilirsin.";
    private List<ChatMessage> messages = new ArrayList<>();
    private final List<String> quickOptions = List.of(
            "Bütçeme uygun kitaplar",
            "Polisiye ve gizem",
            "Klasik öner",
            "Roman öner",
            "Çocuk kitapları",
            "Tarih kitapları"
    );

    @EJB
    private BookFacadeLocal bookFacade;

    public String ask() {
        String clean = normalize(question);
        if (clean.isEmpty()) {
            answer = "Bir seçenek seçebilir ya da örneğin '150 TL altı polisiye öner' gibi daha net bir istek yazabilirsin.";
            addAssistantMessage(answer);
            return null;
        }

        addUserMessage(question.trim());
        List<Book> books = bookFacade.findAll();
        if (books.isEmpty()) {
            answer = "Katalogda henüz kitap bulunmuyor. Kitap eklendiğinde burada öneriler göstereceğim.";
            addAssistantMessage(answer);
            question = "";
            return null;
        }

        List<Book> matches = filterBooks(clean, books);
        String intro = chooseIntro(clean, matches);
        if (matches.isEmpty()) {
            matches = books.stream()
                    .filter(book -> book.getStockQuantity() > 0)
                    .sorted(Comparator.comparing(Book::getTitle, Comparator.nullsLast(String::compareToIgnoreCase)))
                    .limit(3)
                    .toList();
            intro = "Tam eşleşen kitap bulamadım; başlamak için bunlara bakabilirsin";
        }

        answer = formatRecommendation(intro, matches);
        addAssistantMessage(answer);
        question = "";
        return null;
    }

    public String chooseOption(String option) {
        question = option;
        return ask();
    }

    private List<Book> filterBooks(String clean, List<Book> books) {
        BigDecimal maxPrice = extractMaxPrice(clean);
        if (maxPrice != null) {
            return books.stream()
                    .filter(book -> book.getPrice() != null && book.getStockQuantity() > 0)
                    .filter(book -> book.getPrice().compareTo(maxPrice) <= 0)
                    .sorted(Comparator.comparing(Book::getPrice))
                    .limit(3)
                    .toList();
        }

        if (containsAny(clean, "en iyi", "populer", "öner", "oner", "ne okuyayim", "ne okuyayım", "tavsiye")) {
            String keyword = detectKeyword(clean);
            if (keyword != null) {
                List<Book> searched = bookFacade.searchBooks(keyword);
                if (!searched.isEmpty()) {
                    return searched.stream()
                            .filter(book -> book.getStockQuantity() > 0)
                            .limit(3)
                            .toList();
                }
            }
        }

        if (containsAny(clean, "ucuz", "butce", "ekonomik", "fiyat")) {
            return books.stream()
                    .filter(book -> book.getPrice() != null && book.getStockQuantity() > 0)
                    .sorted(Comparator.comparing(Book::getPrice))
                    .limit(3)
                    .toList();
        }

        String keyword = detectKeyword(clean);
        if (keyword != null) {
            List<Book> searched = bookFacade.searchBooks(keyword);
            if (!searched.isEmpty()) {
                return searched.stream()
                        .filter(book -> book.getStockQuantity() > 0)
                        .limit(3)
                        .toList();
            }
            return books.stream()
                    .filter(book -> book.getStockQuantity() > 0)
                    .filter(book -> matchesCategory(book, keyword) || contains(normalize(book.getDescription()), keyword))
                    .limit(3)
                    .toList();
        }

        if (containsAny(clean, "stok", "var mi", "mevcut")) {
            return books.stream()
                    .filter(book -> book.getStockQuantity() > 0)
                    .sorted(Comparator.comparing(Book::getStockQuantity).reversed())
                    .limit(3)
                    .toList();
        }

        return books.stream()
                .filter(book -> book.getStockQuantity() > 0)
                .sorted(Comparator.comparing(Book::getPrice, Comparator.nullsLast(BigDecimal::compareTo)))
                .limit(3)
                .toList();
    }

    private String detectKeyword(String clean) {
        if (containsAny(clean, "polisiye", "gizem")) return "Polisiye";
        if (containsAny(clean, "cocuk", "cocuklar")) return "Cocuk";
        if (containsAny(clean, "tarih", "historik")) return "Tarih";
        if (containsAny(clean, "felsefe")) return "Felsefe";
        if (containsAny(clean, "klasik")) return "Klasik";
        if (containsAny(clean, "roman")) return "Roman";
        if (containsAny(clean, "bilim kurgu", "kurgu")) return "Bilim Kurgu";
        if (containsAny(clean, "psikoloji")) return "Psikoloji";
        if (containsAny(clean, "biyografi")) return "Biyografi";
        if (containsAny(clean, "gelisim", "kisisel")) return "Kisisel Gelisim";
        return null;
    }

    private String chooseIntro(String clean, List<Book> matches) {
        if (matches.isEmpty()) {
            return "";
        }
        if (extractMaxPrice(clean) != null) {
            return "Belirttiğin fiyat aralığına uygun öneriler";
        }
        if (containsAny(clean, "ucuz", "butce", "ekonomik", "fiyat")) {
            return "Bütçene uygun öneriler";
        }
        if (containsAny(clean, "stok", "var mi", "mevcut")) {
            return "Stokta olan güçlü seçenekler";
        }
        return "Soruna göre katalogdan seçtiğim kitaplar";
    }

    private String formatRecommendation(String title, List<Book> books) {
        if (books.isEmpty()) {
            return "Şu an uygun bir kitap bulamadım. Katalogda farklı bir tür ya da fiyat aralığı deneyebilirsin.";
        }

        String recommendations = books.stream()
                .limit(3)
                .map(this::formatBook)
                .collect(Collectors.joining("\n"));
        return title + ":\n" + recommendations + "\n\nDaha net seçim için alttaki seçeneklerden birine basabilir ya da kendi isteğini yazabilirsin.";
    }

    private String formatBook(Book book) {
        String author = "";
        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            Author firstAuthor = book.getAuthors().get(0);
            author = " - " + safe(firstAuthor.getName()) + " " + safe(firstAuthor.getSurname());
        }
        BigDecimal price = book.getPrice();
        String priceText = price == null ? "fiyat bilgisi yok" : price + " TL";
        return "- " + safe(book.getTitle()) + author + " (" + priceText + ")";
    }

    private boolean matchesCategory(Book book, String keyword) {
        if (book.getCategories() == null) {
            return false;
        }
        String normalizedKeyword = normalize(keyword);
        for (Category category : book.getCategories()) {
            if (contains(normalize(category.getName()), normalizedKeyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAny(String value, String... needles) {
        for (String needle : needles) {
            if (contains(value, needle)) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(String value, String needle) {
        return value != null && value.contains(normalize(needle));
    }

    private BigDecimal extractMaxPrice(String clean) {
        if (!containsAny(clean, "tl", "lira", "butce", "butceme", "fiyat", "alti", "altinda", "civari")) {
            return null;
        }
        Matcher matcher = Pattern.compile("(\\d{2,5})").matcher(clean);
        if (!matcher.find()) {
            return null;
        }
        return new BigDecimal(matcher.group(1));
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim()
                .toLowerCase(Locale.forLanguageTag("tr"))
                .replace('\u00e7', 'c')
                .replace('\u011f', 'g')
                .replace('\u0131', 'i')
                .replace('\u00f6', 'o')
                .replace('\u015f', 's')
                .replace('\u00fc', 'u');
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private void addUserMessage(String text) {
        messages.add(new ChatMessage(text, true));
        trimMessages();
    }

    private void addAssistantMessage(String text) {
        messages.add(new ChatMessage(text, false));
        trimMessages();
    }

    private void trimMessages() {
        while (messages.size() > MAX_MESSAGES) {
            messages.remove(0);
        }
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<ChatMessage> getMessages() {
        if (messages.isEmpty()) {
            messages.add(new ChatMessage(answer, false));
        }
        return messages;
    }

    public List<String> getQuickOptions() {
        return quickOptions;
    }

    public static class ChatMessage implements Serializable {
        private final String text;
        private final boolean user;

        public ChatMessage(String text, boolean user) {
            this.text = text;
            this.user = user;
        }

        public String getText() {
            return text;
        }

        public boolean isUser() {
            return user;
        }
    }
}
