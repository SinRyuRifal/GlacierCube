/*
PostgreSQL Backup
Database: EsBatuFinal/public
Backup Time: 2023-12-12 14:07:26
*/

DROP TABLE IF EXISTS "public"."bahanbaku";
DROP TABLE IF EXISTS "public"."detailpembelian";
DROP TABLE IF EXISTS "public"."detailpesanan";
DROP TABLE IF EXISTS "public"."gajikaryawan";
DROP TABLE IF EXISTS "public"."jenisproduk";
DROP TABLE IF EXISTS "public"."karyawan";
DROP TABLE IF EXISTS "public"."mesin";
DROP TABLE IF EXISTS "public"."pembayaranpembelian";
DROP TABLE IF EXISTS "public"."pembayaranpesanan";
DROP TABLE IF EXISTS "public"."pembelian";
DROP TABLE IF EXISTS "public"."pesanan";
DROP TABLE IF EXISTS "public"."produk";
DROP TABLE IF EXISTS "public"."produksi";
DROP TABLE IF EXISTS "public"."supplier";
DROP TABLE IF EXISTS "public"."transaksigaji";
DROP FUNCTION IF EXISTS "public"."hapus_data_setelah_produksi(p_kodeproduksi varchar)";
DROP FUNCTION IF EXISTS "public"."perbarui_stok_setelah_produksi(p_id_bahan varchar, p_bahan_diperlukan int4, p_id_produk varchar, p_jumlah_produksi int4)";
CREATE TABLE "bahanbaku" (
  "idbahan" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "namabahan" varchar(100) COLLATE "pg_catalog"."default",
  "stockbahan" int4
)
;
ALTER TABLE "bahanbaku" OWNER TO "postgres";
CREATE TABLE "detailpembelian" (
  "kodedetailbeli" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "idpembelian" varchar(100) COLLATE "pg_catalog"."default",
  "idbahan" varchar(100) COLLATE "pg_catalog"."default",
  "hargabeli" float8,
  "subtotalpembelian" int4,
  "subtotalhargapembelian" float8
)
;
ALTER TABLE "detailpembelian" OWNER TO "postgres";
CREATE TABLE "detailpesanan" (
  "kodedetailpesan" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "idpesanan" varchar(100) COLLATE "pg_catalog"."default",
  "idproduk" varchar(100) COLLATE "pg_catalog"."default",
  "hargajual" float8,
  "subtotalpesanan" int4,
  "subtotalhargapesanan" float8
)
;
ALTER TABLE "detailpesanan" OWNER TO "postgres";
CREATE TABLE "gajikaryawan" (
  "idgaji" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "gajipokok" float8,
  "tunjangan" float8,
  "potongan" float8
)
;
ALTER TABLE "gajikaryawan" OWNER TO "postgres";
CREATE TABLE "jenisproduk" (
  "kodejenis" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "jenises" varchar(100) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "jenisproduk" OWNER TO "postgres";
CREATE TABLE "karyawan" (
  "idkaryawan" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "namakaryawan" varchar(100) COLLATE "pg_catalog"."default",
  "jeniskelamin" char(1) COLLATE "pg_catalog"."default",
  "divisi" varchar(25) COLLATE "pg_catalog"."default",
  "alamatkaryawan" varchar(200) COLLATE "pg_catalog"."default",
  "telpkaryawan" varchar(20) COLLATE "pg_catalog"."default",
  "emailkaryawan" varchar(50) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "karyawan" OWNER TO "postgres";
CREATE TABLE "mesin" (
  "idmesin" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "kodejenis" varchar(100) COLLATE "pg_catalog"."default",
  "namamesin" varchar(100) COLLATE "pg_catalog"."default",
  "beratmesin" float8,
  "tahunmesin" varchar(32) COLLATE "pg_catalog"."default",
  "asalmesin" varchar(50) COLLATE "pg_catalog"."default",
  "merkmesin" varchar(50) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "mesin" OWNER TO "postgres";
CREATE TABLE "pembayaranpembelian" (
  "idpembayaranpembelian" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "idpembelian" varchar(100) COLLATE "pg_catalog"."default",
  "tglpembayaranpembelian" date,
  "totalpembayaranpembelian" float8,
  "sisapembayaranpembelian" float8,
  "kembalianpembayaranpembelian" float8
)
;
ALTER TABLE "pembayaranpembelian" OWNER TO "postgres";
CREATE TABLE "pembayaranpesanan" (
  "idpembayaranpesanan" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "idpesanan" varchar(100) COLLATE "pg_catalog"."default",
  "tglpembayaranpesanan" date,
  "totalpembayaranpesanan" float8,
  "sisapembayaranpesanan" float8,
  "kembalianpembayaranpesanan" float8
)
;
ALTER TABLE "pembayaranpesanan" OWNER TO "postgres";
CREATE TABLE "pembelian" (
  "idpembelian" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "idsupplier" varchar(100) COLLATE "pg_catalog"."default",
  "idkaryawan" varchar(100) COLLATE "pg_catalog"."default",
  "tglpembelian" date,
  "totalpembelian" int4,
  "totalhargapembelian" float8,
  "statuspembelian" varchar(100) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "pembelian" OWNER TO "postgres";
CREATE TABLE "pesanan" (
  "idpesanan" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "idkaryawan" varchar(100) COLLATE "pg_catalog"."default",
  "tglpesanan" date,
  "totalpesanan" int4,
  "totalhargapesanan" float8,
  "statuspesanan" varchar(100) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "pesanan" OWNER TO "postgres";
CREATE TABLE "produk" (
  "idproduk" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "kodejenis" varchar(100) COLLATE "pg_catalog"."default",
  "namaproduk" varchar(100) COLLATE "pg_catalog"."default",
  "beratproduk" int4,
  "stockproduk" int4
)
;
ALTER TABLE "produk" OWNER TO "postgres";
CREATE TABLE "produksi" (
  "kodeproduksi" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "idmesin" varchar(100) COLLATE "pg_catalog"."default",
  "idbahan" varchar(100) COLLATE "pg_catalog"."default",
  "idproduk" varchar(100) COLLATE "pg_catalog"."default",
  "idkaryawan" varchar(100) COLLATE "pg_catalog"."default",
  "tglproduksi" date,
  "jumlahbahan" int4,
  "jumlahproduksi" int4
)
;
ALTER TABLE "produksi" OWNER TO "postgres";
CREATE TABLE "supplier" (
  "idsupplier" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "namasupplier" varchar(100) COLLATE "pg_catalog"."default",
  "tokosupplier" varchar(100) COLLATE "pg_catalog"."default",
  "alamatsupplier" varchar(200) COLLATE "pg_catalog"."default",
  "telpsupplier" varchar(20) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "supplier" OWNER TO "postgres";
CREATE TABLE "transaksigaji" (
  "idtransaksigaji" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "idkaryawan" varchar(100) COLLATE "pg_catalog"."default",
  "idgaji" varchar(100) COLLATE "pg_catalog"."default",
  "tanggalgajian" date,
  "gajibersih" float8
)
;
ALTER TABLE "transaksigaji" OWNER TO "postgres";
CREATE OR REPLACE FUNCTION "hapus_data_setelah_produksi"("p_kodeproduksi" varchar)
  RETURNS "pg_catalog"."void" AS $BODY$
DECLARE
    v_idproduk VARCHAR(255);
    v_idbahan VARCHAR(255);
    v_jumlahproduksi FLOAT;
    v_jumlahbahan FLOAT;
BEGIN

    SELECT idproduk, idbahan, jumlahproduksi, jumlahbahan
    INTO v_idproduk, v_idbahan, v_jumlahproduksi, v_jumlahbahan
    FROM produksi
    WHERE kodeproduksi = p_kodeproduksi;

    DELETE FROM produksi WHERE kodeproduksi = p_kodeproduksi;

    UPDATE produk SET stockproduk = stockproduk - v_jumlahproduksi WHERE idproduk = v_idproduk;

    UPDATE bahanbaku SET stockbahan = stockbahan + v_jumlahbahan WHERE idbahan = v_idbahan;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "hapus_data_setelah_produksi"("p_kodeproduksi" varchar) OWNER TO "postgres";
CREATE OR REPLACE FUNCTION "perbarui_stok_setelah_produksi"("p_id_bahan" varchar, "p_bahan_diperlukan" int4, "p_id_produk" varchar, "p_jumlah_produksi" int4)
  RETURNS "pg_catalog"."bool" AS $BODY$
DECLARE
    stok_sekarang INT;
BEGIN
 
    SELECT stockbahan INTO stok_sekarang FROM bahanbaku WHERE idbahan = p_id_bahan;
    IF stok_sekarang < p_bahan_diperlukan THEN
        RAISE EXCEPTION 'Stok bahan tidak mencukupi untuk produksi.';
    END IF;


    UPDATE produk SET stockproduk = stockproduk + p_jumlah_produksi WHERE idproduk = p_id_produk;

    UPDATE bahanbaku SET stockbahan = stockbahan - p_bahan_diperlukan WHERE idbahan = p_id_bahan;

    RETURN TRUE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "perbarui_stok_setelah_produksi"("p_id_bahan" varchar, "p_bahan_diperlukan" int4, "p_id_produk" varchar, "p_jumlah_produksi" int4) OWNER TO "postgres";
BEGIN;
LOCK TABLE "public"."bahanbaku" IN SHARE MODE;
DELETE FROM "public"."bahanbaku";
INSERT INTO "public"."bahanbaku" ("idbahan","namabahan","stockbahan") VALUES ('BB002', 'Plastik M', 774),('BB003', 'Plastik L', 1173),('BB001', 'Plastik S', 934),('BB004', 'Plastik XL', 357),('BB005', 'Plastik XXL', 393)
;
COMMIT;
BEGIN;
LOCK TABLE "public"."detailpembelian" IN SHARE MODE;
DELETE FROM "public"."detailpembelian";
INSERT INTO "public"."detailpembelian" ("kodedetailbeli","idpembelian","idbahan","hargabeli","subtotalpembelian","subtotalhargapembelian") VALUES ('BELI-20231101-1//SUP-001//BB001', 'BELI-20231101-1', 'BB001', 2000, 3, 6000),('BELI-20231101-1//SUP-001//BB005', 'BELI-20231101-1', 'BB005', 1000, 4, 4000),('BELI-20231120-1//SUP-026//BB004', 'BELI-20231120-1', 'BB004', 2000, 6, 12000),('BELI-20231120-2//SUP-002//BB001', 'BELI-20231120-2', 'BB001', 5000, 2, 10000),('BELI-20231120-2//SUP-002//BB002', 'BELI-20231120-2', 'BB002', 7000, 2, 14000),('BELI-20231121-1//SUP-001//BB005', 'BELI-20231121-1', 'BB005', 2000, 4, 8000),('BELI-20231121-1//SUP-001//BB001', 'BELI-20231121-1', 'BB001', 3000, 9, 27000),('BELI-20231230-1//SUP-011//BB002', 'BELI-20231230-1', 'BB002', 2000, 3, 6000),('BELI-20231230-2//SUP-011//BB002', 'BELI-20231230-2', 'BB002', 3000, 7, 21000),('BELI-20230112-1//SUP-009//BB005', 'BELI-20230112-1', 'BB005', 5000, 12, 60000),('BELI-20230207-1//SUP-008//BB005', 'BELI-20230207-1', 'BB005', 4000, 12, 48000),('BELI-20230315-1//SUP-024//BB002', 'BELI-20230315-1', 'BB002', 7000, 8, 56000),('BELI-20230415-1//SUP-006//BB001', 'BELI-20230415-1', 'BB001', 12000, 8, 96000),('BELI-20230515-1//SUP-040//BB001', 'BELI-20230515-1', 'BB001', 2000, 10, 20000),('BELI-20231216-1//SUP-009//BB002', 'BELI-20231216-1', 'BB002', 2000, 22, 44000),('BELI-20230929-1//SUP-012//BB002', 'BELI-20230929-1', 'BB002', 2500, 32, 80000),('BELI-20231201-1//SUP-007//BB003', 'BELI-20231201-1', 'BB003', 3000, 2, 6000),('BELI-20231210-1//SUP-006//BB001', 'BELI-20231210-1', 'BB001', 3000, 2, 6000),('BELI-20231212-1//SUP-007//BB004', 'BELI-20231212-1', 'BB004', 2000, 2, 4000)
;
COMMIT;
BEGIN;
LOCK TABLE "public"."detailpesanan" IN SHARE MODE;
DELETE FROM "public"."detailpesanan";
INSERT INTO "public"."detailpesanan" ("kodedetailpesan","idpesanan","idproduk","hargajual","subtotalpesanan","subtotalhargapesanan") VALUES ('PESN-20231118-1//PROD-CB01L', 'PESN-20231118-1', 'PROD-CB01L', 7000, 8, 56000),('PESN-20231118-1//PROD-CB01S', 'PESN-20231118-1', 'PROD-CB01S', 7000, 4, 28000),('PESN-20231118-1//PROD-CB01XL', 'PESN-20231118-1', 'PROD-CB01XL', 9000, 7, 63000),('PESN-20231119-1//PROD-CB01XL', 'PESN-20231119-1', 'PROD-CB01XL', 5000, 5, 25000),('PESN-20231119-1//PROD-TB01XL', 'PESN-20231119-1', 'PROD-TB01XL', 7600, 7, 53200),('PESN-20231120-1//PROD-CB01L', 'PESN-20231120-1', 'PROD-CB01L', 2000, 2, 4000),('PESN-20231120-1//PROD-CB01S', 'PESN-20231120-1', 'PROD-CB01S', 3000, 6, 18000),('PESN-20231122-1//PROD-TB01XL', 'PESN-20231122-1', 'PROD-TB01XL', 5000, 7, 35000),('PESN-20231122-1//PROD-CB01XL', 'PESN-20231122-1', 'PROD-CB01XL', 6000, 10, 60000),('PESN-20231121-1//PROD-CB01XL', 'PESN-20231121-1', 'PROD-CB01XL', 7000, 7, 49000),('PESN-20231121-1//PROD-TB01XL', 'PESN-20231121-1', 'PROD-TB01XL', 3000, 6, 18000),('PESN-20231121-2//PROD-CB01XL', 'PESN-20231121-2', 'PROD-CB01XL', 2000, 3, 6000),('PESN-20231121-2//PROD-TB01XL', 'PESN-20231121-2', 'PROD-TB01XL', 2000, 4, 8000),('PESN-20231120-2//PROD-CB01L', 'PESN-20231120-2', 'PROD-CB01L', 7000, 19, 133000),('PESN-20231125-1//PROD-CB01L', 'PESN-20231125-1', 'PROD-CB01L', 2000, 6, 12000),('PESN-20230918-1//PROD-CB01XL', 'PESN-20230918-1', 'PROD-CB01XL', 5000, 2, 10000),('PESN-20230918-1//PROD-TB01XL', 'PESN-20230918-1', 'PROD-TB01XL', 7000, 10, 70000),('PESN-20230903-1//PROD-CB01XL', 'PESN-20230903-1', 'PROD-CB01XL', 4000, 10, 40000),('PESN-20231002-1//PROD-TB01XL', 'PESN-20231002-1', 'PROD-TB01XL', 5000, 9, 45000),('PESN-20230816-1//PROD-CB01L', 'PESN-20230816-1', 'PROD-CB01L', 9000, 6, 54000),('PESN-20230112-1//PROD-CB01XL', 'PESN-20230112-1', 'PROD-CB01XL', 5000, 19, 95000),('PESN-20230120-1//PROD-TB01XXL', 'PESN-20230120-1', 'PROD-TB01XXL', 2000, 7, 14000),('PESN-20230202-1//PROD-CB01M', 'PESN-20230202-1', 'PROD-CB01M', 7000, 10, 70000),('PESN-20230202-2//PROD-CB01M', 'PESN-20230202-2', 'PROD-CB01M', 3000, 20, 60000),('PESN-20230315-1//PROD-CB01M', 'PESN-20230315-1', 'PROD-CB01M', 10000, 10, 100000),('PESN-20230420-1//PROD-CB01M', 'PESN-20230420-1', 'PROD-CB01M', 3000, 10, 30000),('PESN-20230615-1//PROD-CB01XL', 'PESN-20230615-1', 'PROD-CB01XL', 2220, 12, 26640),('PESN-20230112-2//PROD-CB01XL', 'PESN-20230112-2', 'PROD-CB01XL', 5000, 7, 35000),('PESN-20230131-1//PROD-CB01XXL', 'PESN-20230131-1', 'PROD-CB01XXL', 6000, 100, 600000),('PESN-20230119-1//PROD-TB01L', 'PESN-20230119-1', 'PROD-TB01L', 7000, 30, 210000),('PESN-20230202-3//PROD-CB01XL', 'PESN-20230202-3', 'PROD-CB01XL', 5000, 21, 105000),('PESN-20230214-1//PROD-CB01XL', 'PESN-20230214-1', 'PROD-CB01XL', 5000, 20, 100000),('PESN-20230214-2//PROD-CR01XL', 'PESN-20230214-2', 'PROD-CR01XL', 5000, 100, 500000),('PESN-20230222-1//PROD-NU01XL', 'PESN-20230222-1', 'PROD-NU01XL', 5000, 100, 500000),('PESN-20230228-1//PROD-NU01XXL', 'PESN-20230228-1', 'PROD-NU01XXL', 7000, 20, 140000),('PESN-20230301-1//PROD-NU01L', 'PESN-20230301-1', 'PROD-NU01L', 6000, 40, 240000),('PESN-20230308-1//PROD-TB01M', 'PESN-20230308-1', 'PROD-TB01M', 5000, 10, 50000),('PESN-20230308-1//PROD-CR01L', 'PESN-20230308-1', 'PROD-CR01L', 9000, 1000, 9000000),('PESN-20230308-1//PROD-BK01XXL', 'PESN-20230308-1', 'PROD-BK01XXL', 6000, 80, 480000),('PESN-20230422-1//PROD-CB01XL', 'PESN-20230422-1', 'PROD-CB01XL', 1000, 20, 20000),('PESN-20230422-1//PROD-CR01XL', 'PESN-20230422-1', 'PROD-CR01XL', 10000, 8, 80000),('PESN-20230422-1//PROD-CR01M', 'PESN-20230422-1', 'PROD-CR01M', 5000, 10, 50000),('PESN-20230502-1//PROD-TB01L', 'PESN-20230502-1', 'PROD-TB01L', 5000, 20, 100000),('PESN-20230502-1//PROD-CR01S', 'PESN-20230502-1', 'PROD-CR01S', 3000, 100, 300000),('PESN-20230531-1//PROD-BK01L', 'PESN-20230531-1', 'PROD-BK01L', 5000, 100, 500000),('PESN-20230531-1//PROD-CR01XXL', 'PESN-20230531-1', 'PROD-CR01XXL', 4000, 100, 400000),('PESN-20231216-1//PROD-CB01L', 'PESN-20231216-1', 'PROD-CB01L', 6000, 6, 36000),('PESN-20231212-1//PROD-BK01XXL', 'PESN-20231212-1', 'PROD-BK01XXL', 2000, 20, 40000),('PESN-20231212-2//PROD-CB01XL', 'PESN-20231212-2', 'PROD-CB01XL', 30, 20, 600),('PESN-20231212-3//PROD-BK01XXL', 'PESN-20231212-3', 'PROD-BK01XXL', 3000, 2, 6000),('PESN-20231117-1//PROD-CB01M', 'PESN-20231117-1', 'PROD-CB01M', 2000, 25, 50000),('PESN-20231212-4//PROD-CR01XL', 'PESN-20231212-4', 'PROD-CR01XL', 9000, 92, 828000),('PESN-20231212-5//PROD-BK01XXL', 'PESN-20231212-5', 'PROD-BK01XXL', 2000, 49, 98000),('PESN-20231212-6//PROD-CB01L', 'PESN-20231212-6', 'PROD-CB01L', 3000, 75, 225000),('PESN-20231212-7//PROD-CB01L', 'PESN-20231212-7', 'PROD-CB01L', 3000, 5, 15000),('PESN-20231004-1//PROD-CB01L', 'PESN-20231004-1', 'PROD-CB01L', 6000, 5, 30000),('PESN-20230612-1//PROD-CB01L', 'PESN-20230612-1', 'PROD-CB01L', 3000, 17, 51000),('PESN-20231212-8//PROD-CB01L', 'PESN-20231212-8', 'PROD-CB01L', 2000, 5, 10000),('PESN-20231212-9//PROD-CB01L', 'PESN-20231212-9', 'PROD-CB01L', 2000, 5, 10000),('PESN-20230713-1//PROD-CB01L', 'PESN-20230713-1', 'PROD-CB01L', 2000, 2, 4000),('PESN-20230906-1//PROD-SL01M', 'PESN-20230906-1', 'PROD-SL01M', 2900, 3, 8700),('PESN-20230913-1//PROD-CB01L', 'PESN-20230913-1', 'PROD-CB01L', 9000, 2, 18000),('PESN-20231012-1//PROD-SL01M', 'PESN-20231012-1', 'PROD-SL01M', 2000, 7, 14000),('PESN-20231202-1//PROD-CB01XL', 'PESN-20231202-1', 'PROD-CB01XL', 3000, 10, 30000)
;
COMMIT;
BEGIN;
LOCK TABLE "public"."gajikaryawan" IN SHARE MODE;
DELETE FROM "public"."gajikaryawan";
INSERT INTO "public"."gajikaryawan" ("idgaji","gajipokok","tunjangan","potongan") VALUES ('Gaji0001', 2000000, 900000, 50000),('Gaji0002', 1500, 2000, 500),('GAJI0003', 3200000, 250000, 20000)
;
COMMIT;
BEGIN;
LOCK TABLE "public"."jenisproduk" IN SHARE MODE;
DELETE FROM "public"."jenisproduk";
INSERT INTO "public"."jenisproduk" ("kodejenis","jenises") VALUES ('JNS-CUBE', 'Es Cube'),('JNS-TUBE', 'Es Tube'),('JNS-CRUSHED', 'Es Crushed'),('JNS-NUGGET', 'Es Nugget'),('JNS-SLAB', 'Es Slab'),('JNS-BLOCK', 'Es Block'),('JNS-SPHERE', 'Es Sphere'),('JNS-FLAKE', 'Es Flake'),('JNS-PELLET', 'Es Pellet')
;
COMMIT;
BEGIN;
LOCK TABLE "public"."karyawan" IN SHARE MODE;
DELETE FROM "public"."karyawan";
INSERT INTO "public"."karyawan" ("idkaryawan","namakaryawan","jeniskelamin","divisi","alamatkaryawan","telpkaryawan","emailkaryawan") VALUES ('KAR0003', 'Rina', 'P', 'Admin', 'Jl. Raya 7', '62895304928374', 'rina@gmail.com'),('KAR0004', 'Budi', 'L', 'Admin', 'Jl. Budi 8', '62895304938475', 'budi@gmail.com'),('KAR0005', 'Citra', 'P', 'Admin', 'Jl. Citra 9', '62895304948576', 'citra@gmail.com'),('KAR0006', 'Deni', 'L', 'Admin', 'Jl. Deni 10', '62895304958677', 'deni@gmail.com'),('KAR0007', 'Eka', 'P', 'Admin', 'Jl. Eka 11', '62895304968778', 'eka@gmail.com'),('KAR0008', 'Fahri', 'L', 'Admin', 'Jl. Fahri 12', '62895304978879', 'fahri@gmail.com'),('KAR0009', 'Gita', 'P', 'Admin', 'Jl. Gita 13', '62895304988980', 'gita@gmail.com'),('KAR0010', 'Hadi', 'L', 'Admin', 'Jl. Hadi 14', '62895304999081', 'hadi@gmail.com'),('KAR0011', 'Ina', 'P', 'Admin', 'Jl. Ina 15', '62895304100182', 'ina@gmail.com'),('KAR0012', 'Joko', 'L', 'Admin', 'Jl. Joko 16', '62895304101183', 'joko@gmail.com'),('KAR0013', 'Kartika', 'P', 'Admin', 'Jl. Kartika 17', '62895304102184', 'kartika@gmail.com'),('KAR0014', 'Luki', 'L', 'Admin', 'Jl. Luki 18', '62895304103185', 'luki@gmail.com'),('KAR0015', 'Mira', 'P', 'Admin', 'Jl. Mira 19', '62895304104186', 'mira@gmail.com'),('KAR0017', 'Oscar', 'L', 'Admin', 'Jl. Oscar 21', '62895304106188', 'oscar@gmail.com'),('KAR0018', 'Putri', 'P', 'Admin', 'Jl. Putri 22', '62895304107189', 'putri@gmail.com'),('KAR0019', 'Qowi', 'L', 'Admin', 'Jl. Qowi 23', '62895304108190', 'qowi@gmail.com'),('KAR0020', 'Rizky', 'L', 'Admin', 'Jl. Rizky 24', '62895304109191', 'rizky@gmail.com'),('KAR0021', 'John Doe', 'L', 'Operator', 'Jl. Contoh 5', '08123456789', 'john.doe@example.com'),('KAR0022', 'Jane Doe', 'P', 'Operator', 'Jl. Contoh 6', '08234567890', 'jane.doe@example.com'),('KAR0023', 'Siti', 'P', 'Operator', 'Jl. Siti 7', '08298765432', 'siti@example.com'),('KAR0024', 'Fajar', 'L', 'Operator', 'Jl. Fajar 8', '08187654321', 'fajar@example.com'),('KAR0025', 'Lia', 'P', 'Operator', 'Jl. Lia 9', '08176543210', 'lia@example.com'),('KAR0026', 'Rudi', 'L', 'Operator', 'Jl. Rudi 10', '08165432109', 'rudi@example.com'),('KAR0027', 'Dewi', 'P', 'Operator', 'Jl. Dewi 11', '08154321098', 'dewi@example.com'),('KAR0028', 'Adi', 'L', 'Operator', 'Jl. Adi 12', '08143210987', 'adi@example.com'),('KAR0029', 'Ratna', 'P', 'Operator', 'Jl. Ratna 13', '08132109876', 'ratna@example.com'),('KAR0030', 'Bambang', 'L', 'Operator', 'Jl. Bambang 14', '08121098765', 'bambang@example.com'),('KAR0031', 'Maya', 'P', 'Operator', 'Jl. Maya 15', '08109987654', 'maya@example.com'),('KAR0032', 'Galih', 'L', 'Operator', 'Jl. Galih 16', '08198876543', 'galih@example.com'),('KAR0033', 'Linda', 'P', 'Operator', 'Jl. Linda 17', '08187765432', 'linda@example.com'),('KAR0034', 'Eko', 'L', 'Operator', 'Jl. Eko 18', '08176654321', 'eko@example.com'),('KAR0035', 'Nina', 'P', 'Operator', 'Jl. Nina 19', '08165543210', 'nina@example.com'),('KAR0036', 'Ricky', 'L', 'Operator', 'Jl. Ricky 20', '08154432109', 'ricky@example.com'),('KAR0040', 'Ivan', 'L', 'Operator', 'Jl. Ivan 24', '08110098765', 'ivan@example.com'),('KAR0002', 'Agus', 'L', 'Admin', 'Manukan Yoso', '62895304918391', 'Ags@gmail.com'),('KAR0001', 'Ageng', 'P', 'Operator', 'Manukan Mukti', '62895304811235', 'Agengwowo@gmail.com'),('KAR0016', 'Nanda', 'L', 'Admin', 'Jl. Nanda 20', '62895304105187', '09010622010@student.uinsby.ac.id'),('KARBaru', 'Albertttt', 'L', 'Admin', 'ManukanMUkti2222', '08954663321', 'Albert@gmail.com')
;
COMMIT;
BEGIN;
LOCK TABLE "public"."mesin" IN SHARE MODE;
DELETE FROM "public"."mesin";
INSERT INTO "public"."mesin" ("idmesin","kodejenis","namamesin","beratmesin","tahunmesin","asalmesin","merkmesin") VALUES ('MESIN-CB01-001', 'JNS-CUBE', 'Mesin Cube 1', 150.5, '2020', 'Indonesia', 'MerkX'),('MESIN-CB01-002', 'JNS-CUBE', 'Mesin Cube 2', 155, '2021', 'China', 'MerkY'),('MESIN-CB01-003', 'JNS-CUBE', 'Mesin Cube 3', 160.2, '2019', 'Korea', 'MerkZ'),('MESIN-CB01-004', 'JNS-CUBE', 'Mesin Cube 4', 148.7, '2020', 'Jepang', 'MerkW'),('MESIN-CB01-005', 'JNS-CUBE', 'Mesin Cube 5', 152.3, '2022', 'Malaysia', 'MerkV'),('MESIN-TB01-001', 'JNS-TUBE', 'Mesin Tube 1', 155, '2020', 'Indonesia', 'MerkXx'),('MESIN-TB01-002', 'JNS-TUBE', 'Mesin Tube 2', 160.5, '2021', 'China', 'MerkYy'),('MESIN-TB01-003', 'JNS-TUBE', 'Mesin Tube 3', 165.7, '2019', 'Korea', 'MerkZz'),('MESIN-TB01-004', 'JNS-TUBE', 'Mesin Tube 4', 158.2, '2020', 'Jepang', 'MerkWw'),('MESIN-TB01-005', 'JNS-TUBE', 'Mesin Tube 5', 162.8, '2022', 'Malaysia', 'MerkVv'),('MESIN-CR01-001', 'JNS-CRUSHED', 'Mesin Crushed 1', 130, '2018', 'Indonesia', 'MerkA'),('MESIN-CR01-002', 'JNS-CRUSHED', 'Mesin Crushed 2', 135.5, '2019', 'China', 'MerkB'),('MESIN-CR01-003', 'JNS-CRUSHED', 'Mesin Crushed 3', 128.7, '2020', 'Korea', 'MerkC'),('MESIN-CR01-004', 'JNS-CRUSHED', 'Mesin Crushed 4', 133.2, '2021', 'Jepang', 'MerkD'),('MESIN-CR01-005', 'JNS-CRUSHED', 'Mesin Crushed 5', 129.8, '2019', 'Malaysia', 'MerkE'),('MESIN-NU01-001', 'JNS-NUGGET', 'Mesin Nugget 1', 120.3, '2022', 'Indonesia', 'MerkF'),('MESIN-NU01-002', 'JNS-NUGGET', 'Mesin Nugget 2', 122.5, '2020', 'China', 'MerkG'),('MESIN-NU01-003', 'JNS-NUGGET', 'Mesin Nugget 3', 118.9, '2021', 'Korea', 'MerkH'),('MESIN-NU01-004', 'JNS-NUGGET', 'Mesin Nugget 4', 124.1, '2018', 'Jepang', 'MerkI'),('MESIN-NU01-005', 'JNS-NUGGET', 'Mesin Nugget 5', 121.7, '2022', 'Malaysia', 'MerkJ'),('MESIN-SL01-001', 'JNS-SLAB', 'Mesin Slab 1', 140, '2023', 'Indonesia', 'MerkK'),('MESIN-SL01-002', 'JNS-SLAB', 'Mesin Slab 2', 145.5, '2022', 'China', 'MerkL'),('MESIN-SL01-003', 'JNS-SLAB', 'Mesin Slab 3', 138.7, '2021', 'Korea', 'MerkM'),('MESIN-SL01-004', 'JNS-SLAB', 'Mesin Slab 4', 143.2, '2020', 'Jepang', 'MerkN'),('MESIN-SL01-005', 'JNS-SLAB', 'Mesin Slab 5', 139.8, '2022', 'Malaysia', 'MerkO'),('MESIN-BK01-001', 'JNS-BLOCK', 'Mesin Block 1', 110, '2019', 'Indonesia', 'MerkP'),('MESIN-BK01-002', 'JNS-BLOCK', 'Mesin Block 2', 115.5, '2020', 'China', 'MerkQ'),('MESIN-BK01-003', 'JNS-BLOCK', 'Mesin Block 3', 108.7, '2021', 'Korea', 'MerkR'),('MESIN-BK01-004', 'JNS-BLOCK', 'Mesin Block 4', 113.2, '2022', 'Jepang', 'MerkS'),('MESIN-BK01-005', 'JNS-BLOCK', 'Mesin Block 5', 109.8, '2023', 'Malaysia', 'MerkT'),('MESIN-SP01-001', 'JNS-SPHERE', 'Mesin Sphere 1', 170, '2020', 'Indonesia', 'MerkU'),('MESIN-SP01-002', 'JNS-SPHERE', 'Mesin Sphere 2', 175.5, '2021', 'China', 'MerkV'),('MESIN-SP01-003', 'JNS-SPHERE', 'Mesin Sphere 3', 168.7, '2019', 'Korea', 'MerkW'),('MESIN-SP01-004', 'JNS-SPHERE', 'Mesin Sphere 4', 173.2, '2020', 'Jepang', 'MerkX'),('MESIN-SP01-005', 'JNS-SPHERE', 'Mesin Sphere 5', 169.8, '2022', 'Malaysia', 'MerkY'),('MESIN-FL01-001', 'JNS-FLAKE', 'Mesin Flake 1', 90, '2021', 'Indonesia', 'MerkZ'),('MESIN-FL01-002', 'JNS-FLAKE', 'Mesin Flake 2', 95.5, '2022', 'China', 'MerkAa'),('MESIN-FL01-003', 'JNS-FLAKE', 'Mesin Flake 3', 88.7, '2020', 'Korea', 'MerkBb'),('MESIN-FL01-004', 'JNS-FLAKE', 'Mesin Flake 4', 93.2, '2023', 'Jepang', 'MerkCc'),('MESIN-FL01-005', 'JNS-FLAKE', 'Mesin Flake 5', 89.8, '2021', 'Malaysia', 'MerkDd'),('MESIN-PL01-001', 'JNS-PELLET', 'Mesin Pellet 1', 100, '2020', 'Indonesia', 'MerkEe'),('MESIN-PL01-002', 'JNS-PELLET', 'Mesin Pellet 2', 105.5, '2021', 'China', 'MerkFf'),('MESIN-PL01-003', 'JNS-PELLET', 'Mesin Pellet 3', 98.7, '2019', 'Korea', 'MerkGg'),('MESIN-PL01-004', 'JNS-PELLET', 'Mesin Pellet 4', 103.2, '2022', 'Jepang', 'MerkHh'),('MESIN-PL01-005', 'JNS-PELLET', 'Mesin Pellet 5', 99.8, '2020', 'Malaysia', 'MerkIi')
;
COMMIT;
BEGIN;
LOCK TABLE "public"."pembayaranpembelian" IN SHARE MODE;
DELETE FROM "public"."pembayaranpembelian";
INSERT INTO "public"."pembayaranpembelian" ("idpembayaranpembelian","idpembelian","tglpembayaranpembelian","totalpembayaranpembelian","sisapembayaranpembelian","kembalianpembayaranpembelian") VALUES ('BYRB-20231101-1', 'BELI-20231101-1', '2023-11-01', 20000, 0, 10000),('BYRB-20231120-1', 'BELI-20231120-1', '2023-11-20', 20000, 0, 8000),('BYRB-20231121-1', 'BELI-20231120-2', '2023-11-21', 30000, 0, 6000),('BYRB-20231125-1', 'BELI-20231121-1', '2023-11-25', 300000, 0, 265000),('BYRB-20231230-1', 'BELI-20231230-1', '2023-12-30', 10000, 0, 4000),('BYRB-20231230-2', 'BELI-20231230-2', '2023-12-30', 200000, 0, 179000),('BYRB-20230117-1', 'BELI-20230112-1', '2023-01-17', 60000, 0, 0),('BYRB-20230217-1', 'BELI-20230207-1', '2023-02-17', 48000, 0, 0),('BYRB-20230315-1', 'BELI-20230315-1', '2023-03-15', 56000, 0, 0),('BYRB-20230415-1', 'BELI-20230415-1', '2023-04-15', 96000, 0, 0),('BYRB-20230523-1', 'BELI-20230515-1', '2023-05-23', 20000, 0, 0),('BYRB-20231223-1', 'BELI-20231216-1', '2023-12-23', 222222, 0, 178222),('BYRB-20231230-3', 'BELI-20230929-1', '2023-12-30', 200000, 0, 120000),('BYRB-20231201-1', 'BELI-20231201-1', '2023-12-01', 3000, 3000, 0),('BYRB-20231216-1', 'BELI-20231201-1', '2023-12-16', 3000, 0, 0),('BYRB-20231211-1', 'BELI-20231210-1', '2023-12-11', 60000, 0, 54000),('BYRB-20231211-2', 'BELI-20231212-1', '2023-12-11', 40, 3960, 0),('BYRB-20231212-1', 'BELI-20231212-1', '2023-12-12', 20000, 0, 16040)
;
COMMIT;
BEGIN;
LOCK TABLE "public"."pembayaranpesanan" IN SHARE MODE;
DELETE FROM "public"."pembayaranpesanan";
INSERT INTO "public"."pembayaranpesanan" ("idpembayaranpesanan","idpesanan","tglpembayaranpesanan","totalpembayaranpesanan","sisapembayaranpesanan","kembalianpembayaranpesanan") VALUES ('BYRP-20231119-1', 'PESN-20231118-1', '2023-11-19', 200000, 0, 53000),('BYRP-20231109-1', 'PESN-20231119-1', '2023-11-09', 80000, 0, 1800),('BYRP-20231120-1', 'PESN-20231120-1', '2023-11-20', 25000, 0, 3000),('BYRP-20231123-1', 'PESN-20231122-1', '2023-11-23', 9500, 85500, 0),('BYR-20231124-1', 'PESN-20231122-1', '2023-11-24', 100000, 0, 14500),('BYRP-20231121-1', 'PESN-20231121-1', '2023-11-21', 100000, 0, 33000),('BYRP-20231121-2', 'PESN-20231121-2', '2023-11-21', 2000, 12000, 0),('BYR-20231125-1', 'PESN-20231121-2', '2023-11-25', 20000, 0, 8000),('BYRP-20231120-2', 'PESN-20231120-2', '2023-11-20', 150000, 0, 17000),('BYRP-20231124-1', 'PESN-20231125-1', '2023-11-24', 2000, 10000, 0),('BYRP-20230919-1', 'PESN-20230918-1', '2023-09-19', 80000, 0, 0),('BYRP-20230912-1', 'PESN-20230903-1', '2023-09-12', 40000, 0, 0),('BYRP-20231004-1', 'PESN-20231002-1', '2023-10-04', 45000, 0, 0),('BYRP-20230821-1', 'PESN-20230816-1', '2023-08-21', 54000, 0, 0),('BYRP-20230112-1', 'PESN-20230112-1', '2023-01-12', 95000, 0, 0),('BYRP-20230119-1', 'PESN-20230120-1', '2023-01-19', 14000, 0, 0),('BYRP-20230202-1', 'PESN-20230202-1', '2023-02-02', 70000, 0, 0),('BYRP-20230202-2', 'PESN-20230202-2', '2023-02-02', 60000, 0, 0),('BYRP-20230322-1', 'PESN-20230315-1', '2023-03-22', 100000, 0, 0),('BYRP-20230422-1', 'PESN-20230420-1', '2023-04-22', 30000, 0, 0),('BYRP-20231215-1', 'PESN-20230615-1', '2023-12-15', 122222, 0, 95582),('BYRP-20230112-2', 'PESN-20230112-2', '2023-01-12', 35000, 0, 0),('BYRP-20230131-1', 'PESN-20230131-1', '2023-01-31', 600000, 0, 0),('BYRP-20230120-1', 'PESN-20230119-1', '2023-01-20', 210000, 0, 0),('BYRP-20230120-2', 'PESN-20230202-3', '2023-01-20', 105000, 0, 0),('BYRP-20230214-1', 'PESN-20230214-1', '2023-02-14', 100000, 0, 0),('BYRP-20230214-2', 'PESN-20230214-2', '2023-02-14', 500000, 0, 0),('BYRP-20230222-1', 'PESN-20230222-1', '2023-02-22', 500000, 0, 0),('BYRP-20230228-1', 'PESN-20230228-1', '2023-02-28', 140000, 0, 0),('BYRP-20230302-1', 'PESN-20230301-1', '2023-03-02', 240000, 0, 0),('BYRP-20230310-1', 'PESN-20230308-1', '2023-03-10', 9530000, 0, 0),('BYRP-20230426-1', 'PESN-20230422-1', '2023-04-26', 150000, 0, 0),('BYRP-20230504-1', 'PESN-20230502-1', '2023-05-04', 400000, 0, 0),('BYRP-20230517-1', 'PESN-20230531-1', '2023-05-17', 900000, 0, 0),('BYR-20231215-1', 'PESN-20231125-1', '2023-12-15', 20000, 0, 10000),('BYRP-20231223-1', 'PESN-20231216-1', '2023-12-23', 200000, 0, 164000),('BYRP-20231213-1', 'PESN-20231212-1', '2023-12-13', 300000, 0, 260000),('BYRP-20231212-1', 'PESN-20231212-2', '2023-12-12', 3000, 0, 2400),('BYRP-20231212-2', 'PESN-20231212-3', '2023-12-12', 20000, 0, 14000),('BYRP-20231212-3', 'PESN-20231117-1', '2023-12-12', 200000, 0, 150000),('BYRP-20231212-4', 'PESN-20231212-4', '2023-12-12', 4000000, 0, 3172000),('BYRP-20231212-5', 'PESN-20231212-5', '2023-12-12', 4000000, 0, 3902000),('BYRP-20231212-6', 'PESN-20231212-6', '2023-12-12', 700000, 0, 475000),('BYRP-20231212-7', 'PESN-20231212-7', '2023-12-12', 20000, 0, 5000),('BYRP-20231019-1', 'PESN-20231004-1', '2023-10-19', 30000, 0, 0),('BYRP-20231212-8', 'PESN-20230612-1', '2023-12-12', 51000, 0, 0),('BYRP-20231212-9', 'PESN-20231212-8', '2023-12-12', 20000, 0, 10000),('BYRP-20231212-10', 'PESN-20231212-9', '2023-12-12', 1000, 9000, 0),('BYRP-20231212-11', 'PESN-20230713-1', '2023-12-12', 200, 3800, 0),('BYR-20231212-1', 'PESN-20230713-1', '2023-12-12', 20000, 0, 16200),('BYRP-20231012-1', 'PESN-20230906-1', '2023-10-12', 2000, 6700, 0),('BYR-20231230-1', 'PESN-20230906-1', '2023-12-30', 30000, 0, 23300),('BYRP-20231213-2', 'PESN-20230913-1', '2023-12-13', 9000, 9000, 0),('BYRP-20231214-1', 'PESN-20231012-1', '2023-12-14', 980, 13020, 0),('BYR-20231212-2', 'PESN-20230913-1', '2023-12-12', 20000, 0, 11000),('BYRP-20231216-1', 'PESN-20231202-1', '2023-12-16', 15000, 15000, 0),('BYR-20231221-1', 'PESN-20231202-1', '2023-12-21', 200000, 0, 185000)
;
COMMIT;
BEGIN;
LOCK TABLE "public"."pembelian" IN SHARE MODE;
DELETE FROM "public"."pembelian";
INSERT INTO "public"."pembelian" ("idpembelian","idsupplier","idkaryawan","tglpembelian","totalpembelian","totalhargapembelian","statuspembelian") VALUES ('BELI-20231101-1', 'SUP-001', 'KAR0003', '2023-11-01', 7, 10000, 'Lunas'),('BELI-20231120-1', 'SUP-026', 'KAR0005', '2023-11-20', 6, 12000, 'Lunas'),('BELI-20231120-2', 'SUP-002', 'KAR0003', '2023-11-20', 4, 24000, 'Lunas'),('BELI-20231121-1', 'SUP-001', 'KAR0003', '2023-11-21', 13, 35000, 'Lunas'),('BELI-20231230-1', 'SUP-011', 'KAR0010', '2023-12-30', 3, 6000, 'Lunas'),('BELI-20231230-2', 'SUP-011', 'KAR0010', '2023-12-30', 7, 21000, 'Lunas'),('BELI-20230112-1', 'SUP-009', 'KAR0006', '2023-01-12', 12, 60000, 'Lunas'),('BELI-20230207-1', 'SUP-008', 'KAR0005', '2023-02-07', 12, 48000, 'Lunas'),('BELI-20230315-1', 'SUP-024', 'KAR0012', '2023-03-15', 8, 56000, 'Lunas'),('BELI-20230415-1', 'SUP-006', 'KAR0007', '2023-04-15', 8, 96000, 'Lunas'),('BELI-20230515-1', 'SUP-040', 'KAR0003', '2023-05-15', 10, 20000, 'Lunas'),('BELI-20231216-1', 'SUP-009', 'KAR0003', '2023-12-16', 22, 44000, 'Lunas'),('BELI-20230929-1', 'SUP-012', 'KAR0006', '2023-09-29', 32, 80000, 'Lunas'),('BELI-20231201-1', 'SUP-007', 'KAR0005', '2023-12-01', 2, 6000, 'Lunas'),('BELI-20231210-1', 'SUP-006', 'KAR0006', '2023-12-10', 2, 6000, 'Lunas'),('BELI-20231212-1', 'SUP-007', 'KAR0007', '2023-12-12', 2, 4000, 'Lunas')
;
COMMIT;
BEGIN;
LOCK TABLE "public"."pesanan" IN SHARE MODE;
DELETE FROM "public"."pesanan";
INSERT INTO "public"."pesanan" ("idpesanan","idkaryawan","tglpesanan","totalpesanan","totalhargapesanan","statuspesanan") VALUES ('PESN-20231118-1', 'KAR0018', '2023-11-18', 19, 147000, 'Lunas'),('PESN-20231119-1', 'KAR0003', '2023-11-19', 12, 78200, 'Lunas'),('PESN-20231120-1', 'KAR0004', '2023-11-20', 8, 22000, 'Lunas'),('PESN-20231122-1', 'KAR0003', '2023-11-22', 17, 95000, 'Lunas'),('PESN-20231121-1', 'KAR0003', '2023-11-21', 13, 67000, 'Lunas'),('PESN-20231121-2', 'KAR0003', '2023-11-21', 7, 14000, 'Lunas'),('PESN-20231120-2', 'KAR0004', '2023-11-20', 19, 133000, 'Lunas'),('PESN-20230918-1', 'KAR0004', '2023-09-18', 12, 80000, 'Lunas'),('PESN-20230903-1', 'KAR0010', '2023-09-03', 10, 40000, 'Lunas'),('PESN-20231002-1', 'KAR0002', '2023-10-02', 9, 45000, 'Lunas'),('PESN-20230816-1', 'KAR0017', '2023-08-16', 6, 54000, 'Lunas'),('PESN-20230112-1', 'KAR0008', '2023-01-12', 19, 95000, 'Lunas'),('PESN-20230120-1', 'KAR0012', '2023-01-20', 7, 14000, 'Lunas'),('PESN-20230202-1', 'KAR0010', '2023-02-02', 10, 70000, 'Lunas'),('PESN-20230202-2', 'KAR0019', '2023-02-02', 20, 60000, 'Lunas'),('PESN-20230315-1', 'KAR0008', '2023-03-15', 10, 100000, 'Lunas'),('PESN-20230420-1', 'KAR0011', '2023-04-20', 10, 30000, 'Lunas'),('PESN-20230615-1', 'KAR0011', '2023-06-15', 12, 26640, 'Lunas'),('PESN-20230112-2', 'KAR0005', '2023-01-12', 7, 35000, 'Lunas'),('PESN-20230131-1', 'KAR0003', '2023-01-31', 100, 600000, 'Lunas'),('PESN-20230119-1', 'KAR0009', '2023-01-19', 30, 210000, 'Lunas'),('PESN-20230202-3', 'KAR0012', '2023-02-02', 21, 105000, 'Lunas'),('PESN-20230214-1', 'KAR0007', '2023-02-14', 20, 100000, 'Lunas'),('PESN-20230214-2', 'KAR0002', '2023-02-14', 100, 500000, 'Lunas'),('PESN-20230222-1', 'KAR0012', '2023-02-22', 100, 500000, 'Lunas'),('PESN-20230228-1', 'KAR0016', '2023-02-28', 20, 140000, 'Lunas'),('PESN-20230301-1', 'KAR0006', '2023-03-01', 40, 240000, 'Lunas'),('PESN-20230308-1', 'KAR0008', '2023-03-08', 1090, 9530000, 'Lunas'),('PESN-20230422-1', 'KAR0019', '2023-04-22', 38, 150000, 'Lunas'),('PESN-20230502-1', 'KAR0011', '2023-05-02', 120, 400000, 'Lunas'),('PESN-20230531-1', 'KAR0011', '2023-05-31', 200, 900000, 'Lunas'),('PESN-20231125-1', 'KAR0005', '2023-11-25', 6, 12000, 'Lunas'),('PESN-20231216-1', 'KAR0005', '2023-12-16', 6, 36000, 'Lunas'),('PESN-20231212-1', 'KAR0005', '2023-12-12', 20, 40000, 'Lunas'),('PESN-20231212-2', 'KAR0006', '2023-12-12', 20, 600, 'Lunas'),('PESN-20231212-3', 'KAR0007', '2023-12-12', 2, 6000, 'Lunas'),('PESN-20231117-1', 'KAR0006', '2023-11-17', 25, 50000, 'Lunas'),('PESN-20231212-4', 'KAR0009', '2023-12-12', 92, 828000, 'Lunas'),('PESN-20231212-5', 'KAR0004', '2023-12-12', 49, 98000, 'Lunas'),('PESN-20231212-6', 'KAR0006', '2023-12-12', 75, 225000, 'Lunas'),('PESN-20231212-7', 'KAR0006', '2023-12-12', 5, 15000, 'Lunas'),('PESN-20231004-1', 'KAR0011', '2023-10-04', 5, 30000, 'Lunas'),('PESN-20230612-1', 'KAR0008', '2023-06-12', 17, 51000, 'Lunas'),('PESN-20231212-8', 'KAR0003', '2023-12-12', 5, 10000, 'Lunas'),('PESN-20231212-9', 'KAR0005', '2023-12-12', 5, 10000, 'Lunas'),('PESN-20230713-1', 'KAR0006', '2023-07-13', 2, 4000, 'Lunas'),('PESN-20230906-1', 'KAR0005', '2023-09-06', 3, 8700, 'Lunas'),('PESN-20231012-1', 'KAR0008', '2023-10-12', 7, 14000, 'Belum Lunas'),('PESN-20230913-1', 'KAR0005', '2023-09-13', 2, 18000, 'Lunas'),('PESN-20231202-1', 'KAR0006', '2023-12-02', 10, 30000, 'Lunas')
;
COMMIT;
BEGIN;
LOCK TABLE "public"."produk" IN SHARE MODE;
DELETE FROM "public"."produk";
INSERT INTO "public"."produk" ("idproduk","kodejenis","namaproduk","beratproduk","stockproduk") VALUES ('PROD-SL01M', 'JNS-SLAB', 'Es Slab M', 3, 797),('PROD-CB01L', 'JNS-CUBE', 'Es Cube L', 5, 646),('PROD-CB01XL', 'JNS-CUBE', 'Es Cube XL', 7, 10),('PROD-NU01S', 'JNS-NUGGET', 'Es Nugget S', 1, 0),('PROD-SL01S', 'JNS-SLAB', 'Es Slab S', 1, 0),('PROD-SL01XL', 'JNS-SLAB', 'Es Slab XL', 7, 0),('PROD-SL01XXL', 'JNS-SLAB', 'Es Slab XXL', 10, 0),('PROD-BK01S', 'JNS-BLOCK', 'Es Block S', 1, 0),('PROD-BK01M', 'JNS-BLOCK', 'Es Block M', 3, 0),('PROD-SP01S', 'JNS-SPHERE', 'Es Sphere S', 1, 0),('PROD-SP01M', 'JNS-SPHERE', 'Es Sphere M', 3, 0),('PROD-SP01L', 'JNS-SPHERE', 'Es Sphere L', 5, 0),('PROD-SP01XL', 'JNS-SPHERE', 'Es Sphere XL', 7, 0),('PROD-SP01XXL', 'JNS-SPHERE', 'Es Sphere XXL', 10, 0),('PROD-FL01S', 'JNS-FLAKE', 'Es Flake S', 1, 0),('PROD-FL01M', 'JNS-FLAKE', 'Es Flake M', 3, 0),('PROD-FL01L', 'JNS-FLAKE', 'Es Flake L', 5, 0),('PROD-FL01XL', 'JNS-FLAKE', 'Es Flake XL', 7, 0),('PROD-FL01XXL', 'JNS-FLAKE', 'Es Flake XXL', 10, 0),('PROD-PL01S', 'JNS-PELLET', 'Es Pellet S', 1, 0),('PROD-PL01M', 'JNS-PELLET', 'Es Pellet M', 3, 0),('PROD-PL01L', 'JNS-PELLET', 'Es Pellet L', 5, 0),('PROD-PL01XL', 'JNS-PELLET', 'Es Pellet XL', 7, 0),('PROD-PL01XXL', 'JNS-PELLET', 'Es Pellet XXL', 10, 0),('PROD-NU01XL', 'JNS-NUGGET', 'Es Nugget XL', 7, 900),('PROD-NU01XXL', 'JNS-NUGGET', 'Es Nugget XXL', 10, 9980),('PROD-NU01L', 'JNS-NUGGET', 'Es Nugget L', 5, 9960),('PROD-TB01M', 'JNS-TUBE', 'Es Tube M', 3, 990),('PROD-CB01S', 'JNS-CUBE', 'Es Cube S', 1, 0),('PROD-CR01L', 'JNS-CRUSHED', 'Es Crushed L', 5, 9000),('PROD-BK01XL', 'JNS-BLOCK', 'Es Block XL', 7, 930),('PROD-CB01XXL', 'JNS-CUBE', 'Es Cube XXL', 10, 9999850),('PROD-CR01M', 'JNS-CRUSHED', 'Es Crushed M', 3, 9990),('PROD-TB01L', 'JNS-TUBE', 'Es Tube L', 5, 50),('PROD-CR01S', 'JNS-CRUSHED', 'Es Crushed S', 1, 900),('PROD-BK01L', 'JNS-BLOCK', 'Es Block L', 5, 900),('PROD-CR01XXL', 'JNS-CRUSHED', 'Es Crushed XXL', 10, 199900),('PROD-TB01XL', 'JNS-TUBE', 'Es Tube XL', 7, 0),('PROD-TB01S', 'JNS-TUBE', 'Es Tube S', 1, 100),('PROD-NU01M', 'JNS-NUGGET', 'Es Nugget M', 3, 1000),('PROD-TB01XXL', 'JNS-TUBE', 'Es Tube XXL', 10, 0),('PROD-SL01L', 'JNS-SLAB', 'Es Slab L', 5, 1000),('PROD-CB01M', 'JNS-CUBE', 'Es Cube M', 3, 25),('PROD-CR01XL', 'JNS-CRUSHED', 'Es Crushed XL', 7, 1800),('PROD-BK01XXL', 'JNS-BLOCK', 'Es Block XXL', 10, 869)
;
COMMIT;
BEGIN;
LOCK TABLE "public"."produksi" IN SHARE MODE;
DELETE FROM "public"."produksi";
INSERT INTO "public"."produksi" ("kodeproduksi","idmesin","idbahan","idproduk","idkaryawan","tglproduksi","jumlahbahan","jumlahproduksi") VALUES ('PROD-20231118-1', 'MESIN-CB01-005', 'BB004', 'PROD-CB01XL', 'KAR0030', '2023-11-18', 200, 200),('PROD-20231119-1', 'MESIN-TB01-004', 'BB004', 'PROD-TB01XL', 'KAR0024', '2023-11-19', 20, 20),('PROD-20231125-1', 'MESIN-CB01-005', 'BB003', 'PROD-CB01L', 'KAR0028', '2023-11-25', 25, 25),('PROD-20231223-1', 'MESIN-TB01-001', 'BB004', 'PROD-TB01XL', 'KAR0021', '2023-12-23', 20, 20),('PROD-20231005-1', 'MESIN-TB01-002', 'BB005', 'PROD-TB01XXL', 'KAR0024', '2023-10-05', 37, 37),('PROD-20231210-1', 'MESIN-CB01-001', 'BB002', 'PROD-CB01M', 'KAR0031', '2023-12-10', 100, 100),('PROD-20231002-1', 'MESIN-TB01-002', 'BB001', 'PROD-TB01S', 'KAR0040', '2023-10-02', 100, 100)
;
COMMIT;
BEGIN;
LOCK TABLE "public"."supplier" IN SHARE MODE;
DELETE FROM "public"."supplier";
INSERT INTO "public"."supplier" ("idsupplier","namasupplier","tokosupplier","alamatsupplier","telpsupplier") VALUES ('SUP-001', 'Andito', 'ARhan Shop Plastic', 'Manukan Yoso', '628953049182013'),('SUP-002', 'Budi Peker', 'Budi Plastic Grocery', 'Manukan Tirta 8Y No 12', '628947195012515'),('SUP-003', 'Citra Jaya', 'Citra Plastic Store', 'Jl. Citra 7', '628953049283214'),('SUP-004', 'Dharma Plastic', 'Dharma Mart', 'Jl. Dharma 15', '628953049384315'),('SUP-005', 'Eka Indah', 'Eka Plastic Supplier', 'Jl. Indah 22', '628953049485416'),('SUP-006', 'Fajar Makmur', 'Fajar Plastic Center', 'Jl. Makmur 14', '628953049586517'),('SUP-007', 'Gita Jaya', 'Gita Plastic Shop', 'Jl. Jaya 10', '628953049687618'),('SUP-008', 'Hadi Plastik', 'Hadi Mart', 'Jl. Plastik 19', '628953049788719'),('SUP-009', 'Ika Supplier', 'Ika Plastic Store', 'Jl. Supplier 25', '628953049889820'),('SUP-010', 'Jaya Plastic', 'Jaya Mart', 'Jl. Plastic 11', '628953049990921'),('SUP-011', 'Karya Plastik', 'Karya Plastic Center', 'Jl. Karya 8', '628953041001022'),('SUP-012', 'Lina Indah', 'Lina Plastic Supplier', 'Jl. Indah 16', '628953041011123'),('SUP-013', 'Maju Jaya', 'Maju Plastic Store', 'Jl. Jaya 7', '628953041021224'),('SUP-014', 'Nusa Plastik', 'Nusa Mart', 'Jl. Plastik 12', '628953041031325'),('SUP-015', 'Oscar Supplier', 'Oscar Plastic Center', 'Jl. Supplier 20', '628953041041426'),('SUP-016', 'Putri Mart', 'Putri Plastic Shop', 'Jl. Mart 18', '628953041051527'),('SUP-017', 'Qowi Jaya', 'Qowi Plastic Store', 'Jl. Jaya 23', '628953041061628'),('SUP-018', 'Rizky Plastik', 'Rizky Plastic Center', 'Jl. Plastik 14', '628953041071729'),('SUP-019', 'Surya Supplier', 'Surya Plastic Shop', 'Jl. Supplier 9', '628953041081830'),('SUP-020', 'Tara Jaya', 'Tara Plastic Center', 'Jl. Jaya 6', '628953041091931'),('SUP-021', 'Udin Plastic', 'Udin Mart', 'Jl. Plastic 5', '628953042001032'),('SUP-022', 'Vina Supplier', 'Vina Plastic Store', 'Jl. Supplier 11', '628953042011133'),('SUP-023', 'Wahyu Plastik', 'Wahyu Plastic Center', 'Jl. Plastik 13', '628953042021234'),('SUP-024', 'Xavi Mart', 'Xavi Plastic Shop', 'Jl. Mart 19', '628953042031335'),('SUP-025', 'Yanti Jaya', 'Yanti Plastic Center', 'Jl. Jaya 17', '628953042041436'),('SUP-026', 'Zainal Plastic', 'Zainal Mart', 'Jl. Plastic 20', '628953042051537'),('SUP-027', 'Agus Plastik', 'Agus Plastic Store', 'Jl. Plastik 25', '628953042061638'),('SUP-028', 'Bunga Supplier', 'Bunga Plastic Center', 'Jl. Supplier 14', '628953042071739'),('SUP-029', 'Cahaya Mart', 'Cahaya Plastic Shop', 'Jl. Mart 8', '628953042081840'),('SUP-030', 'Dewi Jaya', 'Dewi Plastic Store', 'Jl. Jaya 23', '628953042091941'),('SUP-031', 'Eko Plastik', 'Eko Mart', 'Jl. Plastik 12', '628953043002042'),('SUP-032', 'Fani Supplier', 'Fani Plastic Center', 'Jl. Supplier 6', '628953043012143'),('SUP-033', 'Galih Plastik', 'Galih Plastic Shop', 'Jl. Plastic 14', '628953043022244'),('SUP-034', 'Hana Jaya', 'Hana Plastic Store', 'Jl. Jaya 9', '628953043032345'),('SUP-035', 'Iqbal Mart', 'Iqbal Plastic Center', 'Jl. Mart 11', '628953043042446'),('SUP-036', 'Jihan Supplier', 'Jihan Plastic Shop', 'Jl. Supplier 22', '628953043052547'),('SUP-037', 'Kiki Plastik', 'Kiki Mart', 'Jl. Plastik 16', '628953043062648'),('SUP-038', 'Laras Jaya', 'Laras Plastic Center', 'Jl. Jaya 20', '628953043072749'),('SUP-039', 'Maman Mart', 'Maman Plastic Store', 'Jl. Mart 13', '628953043082850'),('SUP-040', 'Nina Supplier', 'Nina Plastic Center', 'Jl. Plastic 7', '628953043092951')
;
COMMIT;
BEGIN;
LOCK TABLE "public"."transaksigaji" IN SHARE MODE;
DELETE FROM "public"."transaksigaji";
INSERT INTO "public"."transaksigaji" ("idtransaksigaji","idkaryawan","idgaji","tanggalgajian","gajibersih") VALUES ('TRXG-20231121-1', 'KAR0003', 'GAJI0003', '2023-11-21', 3430000),('TRXG-20231125-1', 'KAR0012', 'GAJI0003', '2023-11-25', 3430000),('TRXG-20231101-1', 'KAR0003', 'Gaji0001', '2023-11-01', 2850000),('TRXG-20231223-1', 'KAR0006', 'Gaji0002', '2023-12-23', 3000),('TRXG-20231201-1', 'KAR0005', 'Gaji0001', '2023-12-01', 2850000)
;
COMMIT;
ALTER TABLE "bahanbaku" ADD CONSTRAINT "pk_bahanbaku" PRIMARY KEY ("idbahan");
CREATE UNIQUE INDEX "bahanbaku_pk" ON "bahanbaku" USING btree (
  "idbahan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "detailpembelian" ADD CONSTRAINT "pk_detailpembelian" PRIMARY KEY ("kodedetailbeli");
CREATE UNIQUE INDEX "detailpembelian_pk" ON "detailpembelian" USING btree (
  "kodedetailbeli" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_18_fk" ON "detailpembelian" USING btree (
  "idbahan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_21_fk" ON "detailpembelian" USING btree (
  "idpembelian" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "detailpesanan" ADD CONSTRAINT "pk_detailpesanan" PRIMARY KEY ("kodedetailpesan");
CREATE UNIQUE INDEX "detailpesanan_pk" ON "detailpesanan" USING btree (
  "kodedetailpesan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_19_fk" ON "detailpesanan" USING btree (
  "idproduk" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_22_fk" ON "detailpesanan" USING btree (
  "idpesanan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "gajikaryawan" ADD CONSTRAINT "pk_gajikaryawan" PRIMARY KEY ("idgaji");
CREATE UNIQUE INDEX "gajikaryawan_pk" ON "gajikaryawan" USING btree (
  "idgaji" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "jenisproduk" ADD CONSTRAINT "pk_jenisproduk" PRIMARY KEY ("kodejenis");
CREATE UNIQUE INDEX "jenises_pk" ON "jenisproduk" USING btree (
  "kodejenis" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "karyawan" ADD CONSTRAINT "pk_karyawan" PRIMARY KEY ("idkaryawan");
CREATE UNIQUE INDEX "karyawan_pk" ON "karyawan" USING btree (
  "idkaryawan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "mesin" ADD CONSTRAINT "pk_mesin" PRIMARY KEY ("idmesin");
CREATE UNIQUE INDEX "mesin_pk" ON "mesin" USING btree (
  "idmesin" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_16_fk2" ON "mesin" USING btree (
  "kodejenis" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "pembayaranpembelian" ADD CONSTRAINT "pk_pembayaranpembelian" PRIMARY KEY ("idpembayaranpembelian");
CREATE UNIQUE INDEX "pembayaranpembelian_pk" ON "pembayaranpembelian" USING btree (
  "idpembayaranpembelian" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "pembayaranpesanan" ADD CONSTRAINT "pk_pembayaranpesanan" PRIMARY KEY ("idpembayaranpesanan");
CREATE UNIQUE INDEX "pembayaranpesanan_pk" ON "pembayaranpesanan" USING btree (
  "idpembayaranpesanan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_5_fk" ON "pembayaranpesanan" USING btree (
  "idpesanan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "pembelian" ADD CONSTRAINT "pk_pembelian" PRIMARY KEY ("idpembelian");
CREATE INDEX "mengelola2_fk" ON "pembelian" USING btree (
  "idsupplier" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "pembelian_pk" ON "pembelian" USING btree (
  "idpembelian" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_6_fk" ON "pembelian" USING btree (
  "idkaryawan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "pesanan" ADD CONSTRAINT "pk_pesanan" PRIMARY KEY ("idpesanan");
CREATE UNIQUE INDEX "pesanan_pk" ON "pesanan" USING btree (
  "idpesanan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_4_fk" ON "pesanan" USING btree (
  "idkaryawan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "produk" ADD CONSTRAINT "pk_produk" PRIMARY KEY ("idproduk");
CREATE UNIQUE INDEX "produk_pk" ON "produk" USING btree (
  "idproduk" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_17_fk2" ON "produk" USING btree (
  "kodejenis" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "produksi" ADD CONSTRAINT "pk_produksi" PRIMARY KEY ("kodeproduksi");
CREATE UNIQUE INDEX "produksi_pk" ON "produksi" USING btree (
  "kodeproduksi" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_14_fk" ON "produksi" USING btree (
  "idkaryawan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_15_fk" ON "produksi" USING btree (
  "idbahan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_16_fk" ON "produksi" USING btree (
  "idmesin" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_17_fk" ON "produksi" USING btree (
  "idproduk" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "supplier" ADD CONSTRAINT "pk_supplier" PRIMARY KEY ("idsupplier");
CREATE UNIQUE INDEX "supplier_pk" ON "supplier" USING btree (
  "idsupplier" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "transaksigaji" ADD CONSTRAINT "pk_transaksigaji" PRIMARY KEY ("idtransaksigaji");
CREATE INDEX "relationship_1_fk" ON "transaksigaji" USING btree (
  "idkaryawan" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "relationship_2_fk" ON "transaksigaji" USING btree (
  "idgaji" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "transaksigaji_pk" ON "transaksigaji" USING btree (
  "idtransaksigaji" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
ALTER TABLE "detailpembelian" ADD CONSTRAINT "fk_detailpe_memiliki3_pembelia" FOREIGN KEY ("idpembelian") REFERENCES "public"."pembelian" ("idpembelian") ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE "detailpembelian" ADD CONSTRAINT "fk_detailpe_memiliki4_bahanbak" FOREIGN KEY ("idbahan") REFERENCES "public"."bahanbaku" ("idbahan") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "detailpesanan" ADD CONSTRAINT "fk_detailpe_memiliki2_pesanan" FOREIGN KEY ("idpesanan") REFERENCES "public"."pesanan" ("idpesanan") ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE "detailpesanan" ADD CONSTRAINT "fk_detailpe_memiliki_produk" FOREIGN KEY ("idproduk") REFERENCES "public"."produk" ("idproduk") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "mesin" ADD CONSTRAINT "fk_mesin_relations_jenispro" FOREIGN KEY ("kodejenis") REFERENCES "public"."jenisproduk" ("kodejenis") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "pembayaranpembelian" ADD CONSTRAINT "fk_pembayar_memiliki5_pembelia" FOREIGN KEY ("idpembelian") REFERENCES "public"."pembelian" ("idpembelian") ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE "pembayaranpesanan" ADD CONSTRAINT "fk_pembayar_memiliki6_pesanan" FOREIGN KEY ("idpesanan") REFERENCES "public"."pesanan" ("idpesanan") ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE "pembelian" ADD CONSTRAINT "fk_pembelia_melakukan_karyawan" FOREIGN KEY ("idkaryawan") REFERENCES "public"."karyawan" ("idkaryawan") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "pembelian" ADD CONSTRAINT "fk_pembelia_mengelola_supplier" FOREIGN KEY ("idsupplier") REFERENCES "public"."supplier" ("idsupplier") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "pesanan" ADD CONSTRAINT "fk_pesanan_mengelola_karyawan" FOREIGN KEY ("idkaryawan") REFERENCES "public"."karyawan" ("idkaryawan") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "produk" ADD CONSTRAINT "fk_produk_relations_jenispro" FOREIGN KEY ("kodejenis") REFERENCES "public"."jenisproduk" ("kodejenis") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "produksi" ADD CONSTRAINT "fk_produksi_dibutuhka_bahanbak" FOREIGN KEY ("idbahan") REFERENCES "public"."bahanbaku" ("idbahan") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "produksi" ADD CONSTRAINT "fk_produksi_digunakan_mesin" FOREIGN KEY ("idmesin") REFERENCES "public"."mesin" ("idmesin") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "produksi" ADD CONSTRAINT "fk_produksi_diproduks_produk" FOREIGN KEY ("idproduk") REFERENCES "public"."produk" ("idproduk") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "produksi" ADD CONSTRAINT "fk_produksi_melakukan_karyawan" FOREIGN KEY ("idkaryawan") REFERENCES "public"."karyawan" ("idkaryawan") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "transaksigaji" ADD CONSTRAINT "fk_transaks_mendapatk_karyawan" FOREIGN KEY ("idkaryawan") REFERENCES "public"."karyawan" ("idkaryawan") ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE "transaksigaji" ADD CONSTRAINT "fk_transaks_termasuk_gajikary" FOREIGN KEY ("idgaji") REFERENCES "public"."gajikaryawan" ("idgaji") ON DELETE RESTRICT ON UPDATE RESTRICT;
