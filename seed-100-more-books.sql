with source_books as (
    select
        gs as n,
        (array[
            'Sessiz Liman', 'Kuzey Defteri', 'Geceye Kalan', 'Mavi Harita', 'Sonbahar Notlari',
            'Kirik Saatler', 'Uzak Bahce', 'Golge Atlas', 'Yol Ayrimi', 'Cam Kent',
            'Kutuphanedeki Sir', 'Sakli Mektup', 'Denizin Hafizasi', 'Yitik Mevsim', 'Kagit Ev',
            'Ruzgarin Izinde', 'Kucuk Istasyon', 'Eski Sokaklar', 'Zamanin Kapisi', 'Derin Uyku'
        ])[((gs - 1) % 20) + 1] || ' ' || lpad(gs::text, 3, '0') as title,
        '978625' || lpad((800000 + gs)::text, 7, '0') as isbn,
        (array[
            'Modern okur icin akici ve guclu bir anlatim.',
            'Insan iliskileri, umut ve degisim uzerine etkileyici bir kitap.',
            'Surukleyici kurgu, sade dil ve guclu karakterlerle hazirlandi.',
            'Kitap kulubu secimleri icin uygun, tempolu bir okuma.',
            'Duygusal derinligi olan, raflarda one cikacak yeni bir eser.'
        ])[((gs - 1) % 5) + 1] as description,
        (85 + ((gs * 7) % 165))::numeric(10,2) as price,
        (8 + ((gs * 11) % 42)) as stock_quantity,
        'https://placehold.co/300x450/' ||
            (array['111827','0f766e','7c2d12','3730a3','be123c','365314','0f172a','854d0e'])[((gs - 1) % 8) + 1] ||
            '/ffffff?text=' || replace((array[
                'Sessiz Liman', 'Kuzey Defteri', 'Geceye Kalan', 'Mavi Harita', 'Sonbahar Notlari',
                'Kirik Saatler', 'Uzak Bahce', 'Golge Atlas', 'Yol Ayrimi', 'Cam Kent',
                'Kutuphanedeki Sir', 'Sakli Mektup', 'Denizin Hafizasi', 'Yitik Mevsim', 'Kagit Ev',
                'Ruzgarin Izinde', 'Kucuk Istasyon', 'Eski Sokaklar', 'Zamanin Kapisi', 'Derin Uyku'
            ])[((gs - 1) % 20) + 1], ' ', '%20') as image_url,
        (array['Ada','Mert','Elif','Deniz','Selin','Kerem','Nehir','Baran','Derya','Ege'])[((gs - 1) % 10) + 1] as author_name,
        (array['Yilmaz','Kaya','Demir','Arslan','Aydin','Celik','Sahin','Koc','Yildiz','Aksoy'])[((gs - 1) % 10) + 1] as author_surname,
        (array['Roman','Klasik','Turk Edebiyati','Bilim Kurgu','Polisiye','Kisisel Gelisim','Tarih','Cocuk','Felsefe','Biyografi'])[((gs - 1) % 10) + 1] as category_name,
        (array['Can Yayinlari','Yapi Kredi Yayinlari','Pegasus Yayinlari','Iletisim Yayinlari','Kronik Kitap'])[((gs - 1) % 5) + 1] as publisher_name
    from generate_series(1, 100) as gs
),
missing_publishers as (
    insert into publisher (name, address, phone, email)
    select distinct sb.publisher_name, 'Istanbul', '0212 000 00 00', lower(replace(sb.publisher_name, ' ', '')) || '@kitapsatis.local'
    from source_books sb
    where not exists (
        select 1 from publisher p where lower(p.name) = lower(sb.publisher_name)
    )
    returning id, name
),
missing_categories as (
    insert into category (name, description)
    select distinct sb.category_name, sb.category_name || ' kitaplari'
    from source_books sb
    where not exists (
        select 1 from category c where lower(c.name) = lower(sb.category_name)
    )
    returning id, name
),
missing_authors as (
    insert into author (name, surname, biography)
    select distinct sb.author_name, sb.author_surname, 'Katalog yazari'
    from source_books sb
    where not exists (
        select 1 from author a
        where lower(a.name) = lower(sb.author_name)
          and lower(a.surname) = lower(sb.author_surname)
    )
    returning id, name, surname
),
inserted_books as (
    insert into book (title, isbn, description, price, stock_quantity, image_url, publisher_id)
    select sb.title, sb.isbn, sb.description, sb.price, sb.stock_quantity, sb.image_url, p.id
    from source_books sb
    join publisher p on lower(p.name) = lower(sb.publisher_name)
    where not exists (
        select 1 from book b where b.isbn = sb.isbn or lower(b.title) = lower(sb.title)
    )
    returning id, title, isbn
),
all_seed_books as (
    select b.id, b.title, b.isbn
    from book b
    join source_books sb on sb.isbn = b.isbn
)
insert into book_authors (book_id, author_id)
select b.id, a.id
from all_seed_books b
join source_books sb on sb.isbn = b.isbn
join author a on lower(a.name) = lower(sb.author_name) and lower(a.surname) = lower(sb.author_surname)
where not exists (
    select 1 from book_authors ba where ba.book_id = b.id and ba.author_id = a.id
);

with source_books as (
    select
        gs as n,
        '978625' || lpad((800000 + gs)::text, 7, '0') as isbn,
        (array['Roman','Klasik','Turk Edebiyati','Bilim Kurgu','Polisiye','Kisisel Gelisim','Tarih','Cocuk','Felsefe','Biyografi'])[((gs - 1) % 10) + 1] as category_name
    from generate_series(1, 100) as gs
)
insert into book_categories (book_id, category_id)
select b.id, c.id
from source_books sb
join book b on b.isbn = sb.isbn
join category c on lower(c.name) = lower(sb.category_name)
where not exists (
    select 1 from book_categories bc where bc.book_id = b.id and bc.category_id = c.id
);

with source_books as (
    select
        gs as n,
        '978625' || lpad((800000 + gs)::text, 7, '0') as isbn,
        (array['Ada','Mert','Elif','Deniz','Selin','Kerem','Nehir','Baran','Derya','Ege'])[((gs - 1) % 10) + 1] as author_name,
        (array['Yilmaz','Kaya','Demir','Arslan','Aydin','Celik','Sahin','Koc','Yildiz','Aksoy'])[((gs - 1) % 10) + 1] as author_surname
    from generate_series(1, 100) as gs
)
insert into book_authors (book_id, author_id)
select b.id, a.id
from source_books sb
join book b on b.isbn = sb.isbn
join author a on lower(a.name) = lower(sb.author_name) and lower(a.surname) = lower(sb.author_surname)
where not exists (
    select 1 from book_authors ba where ba.book_id = b.id and ba.author_id = a.id
);
