with new_books(title, isbn, description, price, stock_quantity, image_url, author_name, author_surname, category_name, publisher_name) as (
    values
    ('Seker Portakali', '9789750738609', 'Cocukluk ve buyume uzerine dokunakli bir roman.', 95.00, 30, 'https://covers.openlibrary.org/b/isbn/9789750738609-L.jpg', 'Jose', 'Vasconcelos', 'Roman', 'Can Yayinlari'),
    ('Hayvan Ciftligi', '9789750719387', 'Siyasi alegori turunun klasiklerinden.', 90.00, 22, 'https://covers.openlibrary.org/b/isbn/9789750719387-L.jpg', 'George', 'Orwell', 'Klasik', 'Can Yayinlari'),
    ('Kucuk Prens', '9789750724435', 'Her yasa hitap eden modern klasik.', 75.00, 40, 'https://covers.openlibrary.org/b/isbn/9789750724435-L.jpg', 'Antoine', 'Saint-Exupery', 'Klasik', 'Can Yayinlari'),
    ('Dava', '9789750718533', 'Burokrasi ve yabancilasma uzerine bir klasik.', 110.00, 18, 'https://covers.openlibrary.org/b/isbn/9789750718533-L.jpg', 'Franz', 'Kafka', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('Donusum', '9789750718595', 'Gregor Samsa ile baslayan unutulmaz hikaye.', 70.00, 35, 'https://covers.openlibrary.org/b/isbn/9789750718595-L.jpg', 'Franz', 'Kafka', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('Suc ve Ceza', '9789750719370', 'Ahlak, vicdan ve ceza uzerine buyuk roman.', 180.00, 16, 'https://covers.openlibrary.org/b/isbn/9789750719370-L.jpg', 'Fyodor', 'Dostoyevski', 'Klasik', 'Can Yayinlari'),
    ('Karamazov Kardesler', '9789750730795', 'Inanc, aile ve adalet uzerine epik roman.', 240.00, 12, 'https://covers.openlibrary.org/b/id/12236237-L.jpg', 'Fyodor', 'Dostoyevski', 'Klasik', 'Can Yayinlari'),
    ('Yeraltindan Notlar', '9789750718526', 'Modern insanin ic catismalarina dair klasik.', 85.00, 25, 'https://covers.openlibrary.org/b/isbn/9789750718526-L.jpg', 'Fyodor', 'Dostoyevski', 'Klasik', 'Can Yayinlari'),
    ('Anna Karenina', '9780143035008', 'Ask, toplum ve aile uzerine klasik roman.', 210.00, 14, 'https://covers.openlibrary.org/b/isbn/9780143035008-L.jpg', 'Lev', 'Tolstoy', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('Savas ve Baris', '9780140447934', 'Tarih ve insanlik uzerine dev roman.', 280.00, 10, 'https://covers.openlibrary.org/b/id/12222770-L.jpg', 'Lev', 'Tolstoy', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('Notre Damein Kamburu', '9780140443530', 'Paris ve insan ruhu uzerine klasik.', 160.00, 13, 'https://covers.openlibrary.org/b/isbn/9780140443530-L.jpg', 'Victor', 'Hugo', 'Klasik', 'Can Yayinlari'),
    ('Sefiller', '9780451419439', 'Adalet, merhamet ve kurtulus hikayesi.', 260.00, 11, 'https://covers.openlibrary.org/b/isbn/9780451419439-L.jpg', 'Victor', 'Hugo', 'Klasik', 'Can Yayinlari'),
    ('Ugur Bocegi', '9786053607236', 'Cagdas Turk edebiyatindan secme bir roman.', 90.00, 20, 'https://covers.openlibrary.org/b/isbn/9786053607236-L.jpg', 'Ayse', 'Kulin', 'Turk Edebiyati', 'Yapi Kredi Yayinlari'),
    ('Tutunamayanlar', '9789754700114', 'Turk edebiyatinin modern klasiklerinden.', 220.00, 12, 'https://covers.openlibrary.org/b/isbn/9789754700114-L.jpg', 'Oguz', 'Atay', 'Turk Edebiyati', 'Can Yayinlari'),
    ('Tehlikeli Oyunlar', '9789754702095', 'Kimlik ve oyun kavramlari etrafinda bir roman.', 180.00, 14, 'https://placehold.co/300x450/2c3e50/ffffff?text=Tehlikeli%20Oyunlar', 'Oguz', 'Atay', 'Turk Edebiyati', 'Can Yayinlari'),
    ('Ince Memed', '9789750807145', 'Anadolu destani niteliginde roman.', 170.00, 18, 'https://covers.openlibrary.org/b/isbn/9789750807145-L.jpg', 'Yasar', 'Kemal', 'Turk Edebiyati', 'Yapi Kredi Yayinlari'),
    ('Puslu Kitalar Atlasi', '9789754704723', 'Fantastik ve tarihsel bir anlatim.', 125.00, 22, 'https://covers.openlibrary.org/b/id/12319859-L.jpg', 'Ihsan', 'Oktay Anar', 'Roman', 'Can Yayinlari'),
    ('Beyaz Kale', '9789750806599', 'Kimlik ve benzerlik uzerine roman.', 115.00, 17, 'https://covers.openlibrary.org/b/isbn/9789750806599-L.jpg', 'Orhan', 'Pamuk', 'Turk Edebiyati', 'Yapi Kredi Yayinlari'),
    ('Kirmizi Sacli Kadin', '9789750835605', 'Baba-ogul temasi etrafinda kurulan roman.', 105.00, 24, 'https://covers.openlibrary.org/b/isbn/9789750835605-L.jpg', 'Orhan', 'Pamuk', 'Turk Edebiyati', 'Yapi Kredi Yayinlari'),
    ('Benim Adim Kirmizi', '9789754707113', 'Sanat, ask ve cinayet uzerine roman.', 190.00, 13, 'https://covers.openlibrary.org/b/isbn/9789754707113-L.jpg', 'Orhan', 'Pamuk', 'Turk Edebiyati', 'Yapi Kredi Yayinlari'),
    ('Aylak Adam', '9789754700084', 'Kentli bireyin yalnizligi uzerine roman.', 95.00, 20, 'https://covers.openlibrary.org/b/isbn/9789754700084-L.jpg', 'Yusuf', 'Atilgan', 'Turk Edebiyati', 'Can Yayinlari'),
    ('Anayurt Oteli', '9789754700091', 'Kasaba, yalnizlik ve saplanti uzerine roman.', 90.00, 21, 'https://covers.openlibrary.org/b/isbn/9789754700091-L.jpg', 'Yusuf', 'Atilgan', 'Turk Edebiyati', 'Can Yayinlari'),
    ('Calikusu', '9789751000200', 'Ask ve idealler uzerine klasik roman.', 130.00, 19, 'https://covers.openlibrary.org/b/id/12958080-L.jpg', 'Resat Nuri', 'Guntekin', 'Turk Edebiyati', 'Can Yayinlari'),
    ('Yaprak Dokumu', '9789751000224', 'Aile ve toplumsal degisim romani.', 120.00, 18, 'https://covers.openlibrary.org/b/id/12299291-L.jpg', 'Resat Nuri', 'Guntekin', 'Turk Edebiyati', 'Can Yayinlari'),
    ('Ask-i Memnu', '9789754589078', 'Yasak ask ve aile dramina dair klasik.', 150.00, 16, 'https://covers.openlibrary.org/b/isbn/9789754589078-L.jpg', 'Halit Ziya', 'Usakligil', 'Turk Edebiyati', 'Yapi Kredi Yayinlari'),
    ('Eylul', '9789754471120', 'Ilk psikolojik romanlardan biri.', 105.00, 18, 'https://covers.openlibrary.org/b/isbn/9789754471120-L.jpg', 'Mehmet Rauf', 'Mehmet Rauf', 'Turk Edebiyati', 'Can Yayinlari'),
    ('Martin Eden', '9780140187724', 'Yazar olma tutkusu ve sinif catismasi.', 140.00, 22, 'https://covers.openlibrary.org/b/isbn/9780140187724-L.jpg', 'Jack', 'London', 'Klasik', 'Can Yayinlari'),
    ('Beyaz Dis', '9780141321110', 'Vahsi doga ve hayatta kalma hikayesi.', 80.00, 25, 'https://covers.openlibrary.org/b/isbn/9780141321110-L.jpg', 'Jack', 'London', 'Klasik', 'Can Yayinlari'),
    ('Denizler Altinda Yirmi Bin Fersah', '9780140367218', 'Bilim kurgu macerasinin klasiklerinden.', 115.00, 19, 'https://covers.openlibrary.org/b/isbn/9780140367218-L.jpg', 'Jules', 'Verne', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('Dunya Merkezine Yolculuk', '9780140367157', 'Bilim ve macera dolu klasik.', 100.00, 20, 'https://covers.openlibrary.org/b/isbn/9780140367157-L.jpg', 'Jules', 'Verne', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('Zaman Makinesi', '9780141439976', 'Zaman yolculugu anlatilarinin onculerinden.', 85.00, 24, 'https://covers.openlibrary.org/b/isbn/9780141439976-L.jpg', 'H G', 'Wells', 'Klasik', 'Can Yayinlari'),
    ('Cesur Yeni Dunya', '9780060850524', 'Distopik toplum kurgusu.', 125.00, 21, 'https://covers.openlibrary.org/b/isbn/9780060850524-L.jpg', 'Aldous', 'Huxley', 'Klasik', 'Can Yayinlari'),
    ('Fahrenheit 451', '9781451673319', 'Kitaplarin yasaklandigi bir gelecek.', 115.00, 26, 'https://covers.openlibrary.org/b/isbn/9781451673319-L.jpg', 'Ray', 'Bradbury', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('Simyaci', '9780061122415', 'Kisisel yolculuk ve arayis hikayesi.', 100.00, 28, 'https://covers.openlibrary.org/b/id/12296155-L.jpg', 'Paulo', 'Coelho', 'Roman', 'Can Yayinlari'),
    ('Yuzuklerin Efendisi', '9780618640157', 'Fantastik edebiyatin basyapitlarindan.', 300.00, 10, 'https://covers.openlibrary.org/b/isbn/9780618640157-L.jpg', 'J R R', 'Tolkien', 'Roman', 'Yapi Kredi Yayinlari'),
    ('Hobbit', '9780547928227', 'Orta Dunya macerasinin baslangici.', 160.00, 14, 'https://covers.openlibrary.org/b/isbn/9780547928227-L.jpg', 'J R R', 'Tolkien', 'Roman', 'Yapi Kredi Yayinlari'),
    ('Harry Potter ve Felsefe Tasi', '9780747532699', 'Buyuculuk dunyasina ilk adim.', 150.00, 30, 'https://covers.openlibrary.org/b/isbn/9780747532699-L.jpg', 'J K', 'Rowling', 'Roman', 'Can Yayinlari'),
    ('Harry Potter ve Sirlar Odasi', '9780747538493', 'Hogwarts macerasi devam ediyor.', 160.00, 27, 'https://covers.openlibrary.org/b/isbn/9780747538493-L.jpg', 'J K', 'Rowling', 'Roman', 'Can Yayinlari'),
    ('Sherlock Holmes Secmeleri', '9780140439076', 'Dedektif edebiyatinin klasiklerinden.', 130.00, 18, 'https://covers.openlibrary.org/b/isbn/9780140439076-L.jpg', 'Arthur Conan', 'Doyle', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('On Kucuk Zenci', '9780062073488', 'Gizem ve polisiye klasigi.', 120.00, 20, 'https://covers.openlibrary.org/b/isbn/9780062073488-L.jpg', 'Agatha', 'Christie', 'Roman', 'Can Yayinlari'),
    ('Dogu Ekspresinde Cinayet', '9780062073501', 'Poirotun unutulmaz davalarindan.', 115.00, 22, 'https://covers.openlibrary.org/b/isbn/9780062073501-L.jpg', 'Agatha', 'Christie', 'Roman', 'Can Yayinlari'),
    ('Gurur ve Onyargi', '9780141439518', 'Ask ve toplum uzerine klasik roman.', 110.00, 19, 'https://covers.openlibrary.org/b/isbn/9780141439518-L.jpg', 'Jane', 'Austen', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('Jane Eyre', '9780141441146', 'Guc, ask ve bagimsizlik romani.', 130.00, 17, 'https://covers.openlibrary.org/b/isbn/9780141441146-L.jpg', 'Charlotte', 'Bronte', 'Klasik', 'Yapi Kredi Yayinlari'),
    ('Ugur Bocegi ve Kelebek', '9786051850009', 'Cagdas edebiyattan sicak bir hikaye.', 88.00, 23, 'https://covers.openlibrary.org/b/isbn/9786051850009-L.jpg', 'Elif', 'Safak', 'Roman', 'Can Yayinlari'),
    ('Baba ve Pic', '9780143112716', 'Aile hafizasi ve kimlik uzerine roman.', 135.00, 16, 'https://covers.openlibrary.org/b/isbn/9780143112716-L.jpg', 'Elif', 'Safak', 'Roman', 'Can Yayinlari'),
    ('Serenad', '9786050902976', 'Tarih ve ask etrafinda kurulu roman.', 150.00, 20, 'https://covers.openlibrary.org/b/isbn/9786050902976-L.jpg', 'Zulfu', 'Livaneli', 'Turk Edebiyati', 'Yapi Kredi Yayinlari'),
    ('Mutluluk', '9789752890770', 'Toplumsal konulari isleyen roman.', 125.00, 18, 'https://covers.openlibrary.org/b/id/10849180-L.jpg', 'Zulfu', 'Livaneli', 'Turk Edebiyati', 'Yapi Kredi Yayinlari')
),
inserted_authors as (
    insert into author (name, surname, biography)
    select distinct author_name, author_surname, 'Kitap yazari'
    from new_books
    returning id, name, surname
),
inserted_books as (
    insert into book (title, isbn, description, price, stock_quantity, image_url, publisher_id)
    select nb.title, nb.isbn, nb.description, nb.price, nb.stock_quantity, nb.image_url, p.id
    from new_books nb
    join publisher p on p.name = nb.publisher_name
    returning id, title
)
insert into book_authors (book_id, author_id)
select ib.id, a.id
from inserted_books ib
join new_books nb on nb.title = ib.title
join author a on a.name = nb.author_name and a.surname = nb.author_surname;

with book_categories_to_add as (
    select b.id as book_id, c.id as category_id
    from book b
    join (
        values
        ('Seker Portakali', 'Roman'), ('Hayvan Ciftligi', 'Klasik'), ('Kucuk Prens', 'Klasik'),
        ('Dava', 'Klasik'), ('Donusum', 'Klasik'), ('Suc ve Ceza', 'Klasik'),
        ('Karamazov Kardesler', 'Klasik'), ('Yeraltindan Notlar', 'Klasik'), ('Anna Karenina', 'Klasik'),
        ('Savas ve Baris', 'Klasik'), ('Notre Damein Kamburu', 'Klasik'), ('Sefiller', 'Klasik'),
        ('Ugur Bocegi', 'Turk Edebiyati'), ('Tutunamayanlar', 'Turk Edebiyati'), ('Tehlikeli Oyunlar', 'Turk Edebiyati'),
        ('Ince Memed', 'Turk Edebiyati'), ('Puslu Kitalar Atlasi', 'Roman'), ('Beyaz Kale', 'Turk Edebiyati'),
        ('Kirmizi Sacli Kadin', 'Turk Edebiyati'), ('Benim Adim Kirmizi', 'Turk Edebiyati'), ('Aylak Adam', 'Turk Edebiyati'),
        ('Anayurt Oteli', 'Turk Edebiyati'), ('Calikusu', 'Turk Edebiyati'), ('Yaprak Dokumu', 'Turk Edebiyati'),
        ('Ask-i Memnu', 'Turk Edebiyati'), ('Eylul', 'Turk Edebiyati'), ('Martin Eden', 'Klasik'),
        ('Beyaz Dis', 'Klasik'), ('Denizler Altinda Yirmi Bin Fersah', 'Klasik'), ('Dunya Merkezine Yolculuk', 'Klasik'),
        ('Zaman Makinesi', 'Klasik'), ('Cesur Yeni Dunya', 'Klasik'), ('Fahrenheit 451', 'Klasik'),
        ('Simyaci', 'Roman'), ('Yuzuklerin Efendisi', 'Roman'), ('Hobbit', 'Roman'),
        ('Harry Potter ve Felsefe Tasi', 'Roman'), ('Harry Potter ve Sirlar Odasi', 'Roman'), ('Sherlock Holmes Secmeleri', 'Klasik'),
        ('On Kucuk Zenci', 'Roman'), ('Dogu Ekspresinde Cinayet', 'Roman'), ('Gurur ve Onyargi', 'Klasik'),
        ('Jane Eyre', 'Klasik'), ('Ugur Bocegi ve Kelebek', 'Roman'), ('Baba ve Pic', 'Roman'),
        ('Serenad', 'Turk Edebiyati'), ('Mutluluk', 'Turk Edebiyati')
    ) as bc(title, category_name) on bc.title = b.title
    join category c on c.name = bc.category_name
)
insert into book_categories (book_id, category_id)
select book_id, category_id from book_categories_to_add;
