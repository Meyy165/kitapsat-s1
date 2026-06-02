# Rafine Kitap

Rafine Kitap, YZL304 Internet Programlama final projesi icin gelistirilmis Jakarta Faces/XHTML tabanli kitap satis uygulamasidir.

## Teknoloji

- Java 21
- Jakarta EE / Jakarta Faces
- XHTML managed bean yapisi
- JPA ve PostgreSQL
- GlassFish 7
- Maven

## Temel Ozellikler

- Kitap katalogu, detay sayfasi ve arama
- Sepet ve siparis akisi
- Kullanici kayit, giris, profil ve istek listesi
- Admin panelinde kitap, yazar, kategori, yayinevi, yorum, kullanici ve siparis yonetimi
- JPA entity iliskileri ile veritabani modeli
- CRUD islemleri
- Oturum ve admin yetki kontrolu
- PBKDF2 tabanli sifre hash yapisi
- Katalogdan cevap veren Rafine Asistan
- Uygulama acilisinda katalogu 200 kitaba tamamlayan seed yapisi

## Hazir Giris Bilgileri

Admin:

```text
admin@admin.com
admin123
```

Kullanici:

```text
user@user.com
user1234
```

## Calistirma

1. PostgreSQL tarafinda `kitapsatispsql` veritabani hazir olmalidir.
2. GlassFish JDBC kaynagi `jdbc/kitapSatisPSQL` olarak tanimlidir.
3. Proje Maven ile paketlenir:

```powershell
mvn package
```

4. GlassFish uzerine deploy edilir:

```powershell
C:\glassfish7\glassfish\bin\asadmin.bat deploy --force=true --name kitap-satis --contextroot kitap-satis target\kitap-satis-1.0-SNAPSHOT.war
```

5. Uygulama adresi:

```text
http://localhost:9090/kitap-satis/
```

## Degerlendirme Kriterleriyle Iliski

- Calisan proje: GlassFish uzerinde deploy edilebilir web uygulamasi.
- Mimari: XHTML sayfalar, managed bean siniflari, facade katmani ve entity modeli ayrilmistir.
- Veritabani: 20 entity ve iliskili JPA modeli vardir.
- CRUD: Admin panelinde temel ekleme, listeleme, guncelleme ve silme islemleri bulunur.
- Guvenlik: Oturum, rol kontrolu, admin filtreleme ve sifre hash yapisi kullanilir.
- Arayuz: Katalog, sepet, profil, admin paneli ve chat asistan sayfalari kullanici akisini destekler.
