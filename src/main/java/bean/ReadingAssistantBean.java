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
    private String answer = "Merhaba, ben Rafine Asistan. Kitap bulma, sepet, ödeme, giriş, kayıt, favori, destek ve admin işlemleri hakkında yardımcı olabilirim.";
    private List<ChatMessage> messages = new ArrayList<>();
    private final List<String> quickOptions = List.of(
            "Kitap öner",
            "110 TL olanlar",
            "Sepete nasıl eklerim?",
            "Admin girişi",
            "Favoriler nasıl çalışır?",
            "Destek talebi aç"
    );

    @EJB
    private BookFacadeLocal bookFacade;

    public String ask() {
        String clean = normalize(question);
        if (clean.isEmpty()) {
            answer = "Ne yapmak istediğini yazabilirsin. Örneğin: '110 TL olanlar', 'polisiye öner', 'sepete nasıl eklerim', 'admin girişi nasıl yapılır'.";
            addAssistantMessage(answer);
            return null;
        }

        addUserMessage(question.trim());
        answer = buildAnswer(clean);
        addAssistantMessage(answer);
        question = "";
        return null;
    }

    public String chooseOption(String option) {
        question = option;
        return ask();
    }

    private String buildAnswer(String clean) {
        if (containsAny(clean, "merhaba", "selam", "slm", "hey")) {
            return "Merhaba. Sana kitap seçimi, fiyat arama, sepet, ödeme, giriş/kayıt, favoriler, destek ve admin paneli konusunda yardımcı olabilirim.";
        }
        if (containsAny(clean, "admin")) {
            return "Admin paneline girmek için Giriş sayfasından admin hesabıyla oturum açmalısın. Admin kullanıcı giriş yapınca üst menüde Admin bağlantısı görünür. Admin panelinde kitap, yazar, kategori, yayınevi, yorum, kullanıcı ve sipariş yönetimi yapılır.";
        }
        if (containsAny(clean, "giris", "giriş", "login", "oturum")) {
            return "Giriş yapmak için üst menüden Giriş sayfasına git. E-posta ve şifreni yazıp Giriş Yap butonuna bas. Admin hesabı admin paneline, normal kullanıcı kitap listesine yönlenir.";
        }
        if (containsAny(clean, "kayit", "kayıt", "uye", "üye", "hesap ac", "hesap aç")) {
            return "Kayıt olmak için üst menüden Kayıt sayfasına git. Ad, soyad, e-posta ve şifre bilgilerini doldur. Kayıttan sonra normal kullanıcı rolüyle giriş yapabilirsin.";
        }
        if (containsAny(clean, "sepet", "sepete", "ekle")) {
            return "Kitaplar sayfasında stokta olan kitapların yanında Sepet butonu bulunur. Sepete ekledikten sonra üst menüdeki Sepet bağlantısından ürünleri görebilir ve ödeme adımına geçebilirsin.";
        }
        if (containsAny(clean, "odeme", "ödeme", "checkout", "siparis", "sipariş", "satın", "satin")) {
            return "Sepet sayfasından Ödemeye Geç butonuyla ödeme adımına ilerlersin. Sipariş tamamlandığında onay sayfası açılır ve sipariş bilgileri gösterilir.";
        }
        if (containsAny(clean, "favori", "istek", "wishlist", "begendim", "beğendim")) {
            return "Kitaplar sayfasındaki Favori butonu kitabı istek listene ekler. Giriş yaptıysan üst menüden İstek Listem sayfasına giderek favorilerini görebilir ve silebilirsin.";
        }
        if (containsAny(clean, "destek", "yardim", "yardım", "talep", "sorun", "şikayet", "sikayet")) {
            return "Destek sayfasından konu ve mesaj yazarak destek talebi oluşturabilirsin. Daha önce açtığın destek talepleri de aynı sayfada listelenir.";
        }
        if (containsAny(clean, "profil", "sifre", "şifre", "adres", "telefon")) {
            return "Giriş yaptıktan sonra Profil sayfasından ad, soyad, e-posta, telefon, adres ve şifre bilgilerini güncelleyebilirsin.";
        }

        if (isCatalogQuestion(clean)) {
            return answerCatalogQuestion(clean);
        }

        return "Bunu net anlayamadım. Sana bu uygulamada kitap bulma, fiyat sorgulama, sepet, ödeme, giriş/kayıt, favoriler, destek ve admin paneli konularında yardımcı olabilirim. İstersen daha açık yaz: '120 TL civarı kitap', 'polisiye öner', 'sepete nasıl eklerim' gibi.";
    }

    private boolean isCatalogQuestion(String clean) {
        return extractPriceIntent(clean) != null
                || detectKeyword(clean) != null
                || containsAny(clean, "kitap", "oku", "okuma", "okuyayim", "okuyayım", "oner", "öner",
                "tavsiye", "ucuz", "butce", "butceme", "fiyat", "tl", "lira", "stok", "mevcut",
                "hediye", "cocuk", "çocuk", "genclik", "yas", "yaş", "baslangic", "başlangıç");
    }

    private String answerCatalogQuestion(String clean) {
        List<Book> books = bookFacade.findAll();
        if (books.isEmpty()) {
            return "Katalogda henüz kitap bulunmuyor. Kitap eklendiğinde öneri ve fiyat sorgusu yapabilirim.";
        }

        List<Book> matches = filterBooks(clean, books);
        String intro = chooseIntro(clean, matches);
        if (matches.isEmpty()) {
            return "Bu kritere uygun kitap bulamadım. Farklı bir tür, fiyat veya daha genel bir istek yazabilirsin.";
        }
        return formatRecommendation(intro, matches);
    }

    private List<Book> filterBooks(String clean, List<Book> books) {
        PriceIntent priceIntent = extractPriceIntent(clean);
        String keyword = detectKeyword(clean);

        return books.stream()
                .filter(book -> book.getStockQuantity() > 0)
                .filter(book -> priceIntent == null || book.getPrice() != null && matchesPriceIntent(book.getPrice(), priceIntent))
                .filter(book -> keyword == null || matchesBookKeyword(book, keyword))
                .sorted(bookComparator(priceIntent))
                .limit(3)
                .toList();
    }

    private boolean matchesBookKeyword(Book book, String keyword) {
        String normalizedKeyword = normalize(keyword);
        return contains(normalize(book.getTitle()), normalizedKeyword)
                || contains(normalize(book.getDescription()), normalizedKeyword)
                || matchesCategory(book, keyword);
    }

    private String detectKeyword(String clean) {
        if (containsAny(clean, "polisiye", "gizem")) return "Polisiye";
        if (containsAny(clean, "cocuk", "çocuk", "cocuklar", "çocuklar")) return "Cocuk";
        if (containsAny(clean, "tarih", "historik")) return "Tarih";
        if (containsAny(clean, "felsefe")) return "Felsefe";
        if (containsAny(clean, "klasik")) return "Klasik";
        if (containsAny(clean, "roman")) return "Roman";
        if (containsAny(clean, "bilim kurgu", "kurgu")) return "Bilim Kurgu";
        if (containsAny(clean, "psikoloji")) return "Psikoloji";
        if (containsAny(clean, "biyografi")) return "Biyografi";
        if (containsAny(clean, "gelisim", "gelişim", "kisisel", "kişisel")) return "Kisisel Gelisim";
        return null;
    }

    private String chooseIntro(String clean, List<Book> matches) {
        if (matches.isEmpty()) {
            return "";
        }
        PriceIntent priceIntent = extractPriceIntent(clean);
        String keyword = detectKeyword(clean);
        if (priceIntent != null && keyword != null) {
            return keyword + " türünde fiyat isteğine uygun kitaplar";
        }
        if (priceIntent != null) {
            if (priceIntent.mode == PriceMode.EXACT) {
                return priceIntent.price + " TL fiyatındaki kitaplar";
            }
            if (priceIntent.mode == PriceMode.AROUND) {
                return priceIntent.price + " TL civarındaki kitaplar";
            }
            return "Belirttiğin bütçeye uygun kitaplar";
        }
        if (keyword != null) {
            return keyword + " türünde öneriler";
        }
        if (containsAny(clean, "stok", "var mi", "var mı", "mevcut")) {
            return "Stokta olan kitaplar";
        }
        return "Katalogdan seçtiğim kitaplar";
    }

    private String formatRecommendation(String title, List<Book> books) {
        String recommendations = books.stream()
                .limit(3)
                .map(this::formatBook)
                .collect(Collectors.joining("\n"));
        return title + ":\n" + recommendations + "\n\nİstersen tür, bütçe veya kullanım amacını biraz daha net yaz; listeyi daraltayım.";
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

    private PriceIntent extractPriceIntent(String clean) {
        if (!containsAny(clean, "tl", "lira", "butce", "butceme", "bütçe", "bütçeme", "fiyat", "alti", "altı", "altinda", "altında", "civari", "civarı")) {
            return null;
        }
        Matcher matcher = Pattern.compile("(\\d{2,5})").matcher(clean);
        if (!matcher.find()) {
            return null;
        }
        BigDecimal price = new BigDecimal(matcher.group(1));
        if (containsAny(clean, "alti", "altı", "altinda", "altında", "en fazla", "maksimum", "butce", "butceme", "bütçe", "bütçeme")) {
            return new PriceIntent(price, PriceMode.MAX);
        }
        if (containsAny(clean, "civari", "civarı", "yaklasik", "yaklaşık", "yakın", "yakin")) {
            return new PriceIntent(price, PriceMode.AROUND);
        }
        return new PriceIntent(price, PriceMode.EXACT);
    }

    private boolean matchesPriceIntent(BigDecimal bookPrice, PriceIntent intent) {
        if (intent.mode == PriceMode.MAX) {
            return bookPrice.compareTo(intent.price) <= 0;
        }
        if (intent.mode == PriceMode.AROUND) {
            BigDecimal lower = intent.price.subtract(BigDecimal.TEN);
            BigDecimal upper = intent.price.add(BigDecimal.TEN);
            return bookPrice.compareTo(lower) >= 0 && bookPrice.compareTo(upper) <= 0;
        }
        return bookPrice.compareTo(intent.price) == 0;
    }

    private Comparator<Book> bookComparator(PriceIntent intent) {
        if (intent != null && intent.mode == PriceMode.AROUND) {
            return Comparator.comparing(book -> book.getPrice().subtract(intent.price).abs());
        }
        if (intent != null) {
            return Comparator.comparing(Book::getPrice);
        }
        return Comparator.comparing(Book::getTitle, Comparator.nullsLast(String::compareToIgnoreCase));
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

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim()
                .toLowerCase(Locale.forLanguageTag("tr"))
                .replace('ç', 'c')
                .replace('ğ', 'g')
                .replace('ı', 'i')
                .replace('ö', 'o')
                .replace('ş', 's')
                .replace('ü', 'u');
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

    private enum PriceMode {
        EXACT,
        MAX,
        AROUND
    }

    private static class PriceIntent {
        private final BigDecimal price;
        private final PriceMode mode;

        private PriceIntent(BigDecimal price, PriceMode mode) {
            this.price = price;
            this.mode = mode;
        }
    }
}
