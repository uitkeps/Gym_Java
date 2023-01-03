ALTER SESSION SET NLS_DATE_FORMAT ='DD/MM/YYYY HH24:MI:SS';

UPDATE SIGNUP_CLASS 
SET class_id = 6 
WHERE payment_id = 463 AND cus_id = 385 AND class_id = 17 

DROP TABLE customer;
DROP TABLE membership;
DROP TABLE class;
DROP TABLE signup_class;
DROP TABLE signup_membership; 
DROP TABLE staff;
DROP TABLE room;
DROP TABLE staff_class;
DROP TABLE DISCOUNT;
DROP TABLE INBODY;
DROP TABLE PAYMENT;
DROP TABLE PRODUCT;
DROP TABLE PAYMENT_DETAIL;

DROP SEQUENCE CLASS_SEQ;
DROP SEQUENCE CUSTOMER_SEQ;
DROP SEQUENCE DISCOUNT_SEQ;
DROP SEQUENCE MEMBERSHIP_SEQ;
DROP SEQUENCE PAYMENT_SEQ;
DROP SEQUENCE PRODUCT_SEQ;
DROP SEQUENCE ROOM_SEQ;
DROP SEQUENCE STAFF_SEQ;

DELETE FROM STAFF_CLASS;
DELETE FROM SIGNUP_CLASS;
DELETE FROM PAYMENT_DETAIL;
DELETE FROM SIGNUP_MEMBERSHIP;
DELETE FROM INBODY;
DELETE FROM MEMBERSHIP;
DELETE FROM ROOM;
DELETE FROM CUSTOMER;
DELETE FROM DISCOUNT;
DELETE FROM PAYMENT;
DELETE FROM STAFF;
DELETE FROM CLASS;
DELETE FROM PRODUCT;

----------------------------
------- CREATE TABLE -------
----------------------------

---- BANG CUSTOMER ----
CREATE TABLE customer
(
    cus_id          number NOT NULL,
    cus_name        varchar2(50) NOT NULL,
    cus_gender      varchar2(5),
    cus_weight      float, --kg
    cus_height      float, --m
    cus_datejoin    date,
    cus_type        varchar2(50) DEFAULT 'Thân thiết',
    cus_memexpired  date,
    cus_birthday    date,
    cus_address     varchar2(100),
    cus_telephone   number(10) NOT NULL unique,
    cus_password    varchar2(100) NOT NULL,
    cus_revenue     int default 0,
    CONSTRAINT PK_CUSTOMER PRIMARY KEY(cus_id)
);
CREATE SEQUENCE CUSTOMER_seq START WITH 1 INCREMENT BY 1;

----- BANG STAFF ----
CREATE TABLE staff
(
    staff_id            number NOT NULL,
    staff_name          varchar2(50),
    staff_birthday      date,
    staff_type          varchar2(50) NOT NULL,
    staff_experience    number,
    staff_expertise     varchar2(50),
    staff_datejoin      date,
    staff_gender        varchar2(5) NOT NULL,
    staff_address       varchar2(100),
    staff_telephone     number(10) NOT NULL unique,
    staff_password      varchar2(100) NOT NULL,
    staff_salary        int NOT NULL,
    CONSTRAINT PK_STAFF PRIMARY KEY(staff_id)    
);
CREATE SEQUENCE STAFF_seq START WITH 1 INCREMENT BY 1;

-------- BANG ROOM -------
CREATE TABLE room
(
    room_id             number,
    room_capacity       number,
    CONSTRAINT PK_ROOM PRIMARY KEY(room_id)  
);
CREATE SEQUENCE ROOM_seq START WITH 1 INCREMENT BY 1;

-------- BANG CLASS -------
CREATE TABLE CLASS
(
    class_id            number NOT NULL,
    class_period        varchar2(70),
    class_date          date,
    class_title         varchar2(50),
    class_cost          int,
    room_id             number CONSTRAINT FK_class_room REFERENCES room(room_id),
    CONSTRAINT PK_CLASS PRIMARY KEY(class_id)  
);
CREATE SEQUENCE CLASS_seq START WITH 1 INCREMENT BY 1;

------ BANG PRODUCT -------
CREATE TABLE product
(
    product_id          number NOT NULL,
    product_name        varchar2(100),
    product_price       int,
    product_type        varchar2(30),
    product_amount      number,
    CONSTRAINT PK_PRODUCT PRIMARY KEY(product_id)
);
CREATE SEQUENCE PRODUCT_seq START WITH 1 INCREMENT BY 1;

------ BANG MEMBERSHIP ------
CREATE TABLE MEMBERSHIP
(
    mem_id              number NOT NULL,
    mem_fee             int,
    mem_level           varchar2(20) DEFAULT 'Đồng' NOT NULL,
    mem_bonus           varchar2(300),
    CONSTRAINT PK_MEMBERSHIP PRIMARY KEY (mem_id)
);
CREATE SEQUENCE MEMBERSHIP_seq START WITH 1 INCREMENT BY 1;

------ BANG DISCOUNT -----
CREATE TABLE discount
(
    dis_id              number NOT NULL,
    dis_code            varchar2(20) NOT NULL unique,
    dis_percent         int,
    CONSTRAINT PK_DISCOUNT PRIMARY KEY (dis_id)
);
CREATE SEQUENCE DISCOUNT_seq START WITH 1 INCREMENT BY 1;

----- BANG PAYMENT -----
CREATE TABLE payment
(
    payment_id          number NOT NULL,
    payment_lastday     date,
    payment_mode        varchar2(50),
    cus_id              number CONSTRAINT FK_payment_customer REFERENCES customer(cus_id),
    dis_id              number CONSTRAINT FK_payment_discount REFERENCES discount(dis_id),
    staff_id            number CONSTRAINT FK_payment_staff REFERENCES staff(staff_id),
    total               int DEFAULT 0,
    CONSTRAINT PK_PAYMENT PRIMARY KEY (payment_id)
);
CREATE SEQUENCE PAYMENT_seq START WITH 1 INCREMENT BY 1;

------ BANG SIGNUP_MEMBERSHIP ------
CREATE TABLE SIGNUP_MEMBERSHIP
(
    payment_id          number CONSTRAINT FK_signup_mempay REFERENCES payment(payment_id),
    mem_id              number CONSTRAINT FK_signup_mem REFERENCES membership(mem_id),
    cus_id              number CONSTRAINT FK_signup_customer REFERENCES customer(cus_id),
    signup_date         date,
    CONSTRAINT PK_SIGNUP_MEMBERSHIP PRIMARY KEY (payment_id, mem_id, cus_id)
);

------ BANG PAYMENT_DETAIL --------
CREATE TABLE payment_detail
(
    payment_id          number CONSTRAINT FK_paydetail_customer REFERENCES payment(payment_id),
    product_id          number CONSTRAINT FK_paydetail_product REFERENCES product(product_id), 
    amount              number,
    CONSTRAINT PK_payment_detail PRIMARY KEY (payment_id, product_id)
);

----- BANG SIGNUP_CLASS -----
CREATE TABLE signup_class
(
    payment_id          number CONSTRAINT FK_signup_payment REFERENCES payment(payment_id),
    cus_id              number CONSTRAINT FK_signup_cus REFERENCES customer(cus_id),
    class_id            number CONSTRAINT FK_signup_class REFERENCES class(class_id),
    signup_date         date,
    CONSTRAINT PK_signup_class PRIMARY KEY (payment_id, cus_id, class_id)
);
--
----- BANG STAFF_CLASS -----
CREATE TABLE staff_class
(
    class_id             number CONSTRAINT FK_staff_class REFERENCES class(class_id),
    staff_id             number CONSTRAINT FK_class_staff REFERENCES staff(staff_id),
    CONSTRAINT PK_STAFF_CLASS PRIMARY KEY (class_id, staff_id)
);

----- BANG INBODY -----
CREATE TABLE INBODY
(
    cus_id              number CONSTRAINT FK_inbody_cus REFERENCES customer(cus_id),
    inbody_date         date,
    muscle_mass         float,
    body_fat_mass       float,
    weight              float, -- kg
    height              float, -- m
    waist               float, -- bụng
    hip                 float, -- hông
    protein             float,
    mineral             float,
    TBW                 float,
    BMI                 float,
    PBF                 float,
    WHR                 float,
    inbody_score        float,
    CONSTRAINT PK_INBODY PRIMARY KEY (cus_id, inbody_date)
);

----------------------------
----- CONSTRAINT CHECK -----
----------------------------

------------ CUSTOMER -------------
-- CUSTOMER_GENDER:
-------- 2 loai: Nam va Nu
ALTER TABLE CUSTOMER
ADD CHECK (CUS_GENDER IN ('Nam', 'Nữ'));

-------- Ngay sinh < Ngay dang ky
ALTER TABLE CUSTOMER
ADD CHECK (CUS_BIRTHDAY < CUS_DATEJOIN);

-- CUSTOMER_TYPE:
-------- 3 loai: Thân thiết, VIP, SUPER VIP
ALTER TABLE CUSTOMER
ADD CHECK (CUS_TYPE IN ('Thân thiết', 'VIP', 'Super VIP'));

------------ MEMBERSHIP -------------
-- MEM_LEVEL: 
-------- 3 loai: Vang, Bac, Dong
ALTER TABLE MEMBERSHIP
ADD CHECK (MEM_LEVEL IN ('Vàng', 'Bạc', 'Đồng'));

------------ CLASS -------------
ALTER TABLE CLASS
ADD CHECK (class_period in ('5h30 - 7h00', '17h30 - 19h00'));

DELETE FROM CLASS WHERE CLASS_ID = 1;
------------ STAFF -------------
-- STAFF_GENDER:
-------- 2 loai: Nam va Nu
ALTER TABLE STAFF
ADD CHECK (STAFF_GENDER IN ('Nam', 'Nữ'));

-- STAFF_TYPE
-------- 3 loai: Huan luyen vien, Thu ngan, Quan ly
ALTER TABLE STAFF
ADD CHECK (STAFF_TYPE IN ('Huấn luyện viên', 'Thu ngân', 'Quản lý'));

-------- Ngay sinh < Ngay vao lam
ALTER TABLE STAFF
ADD CHECK (staff_birthday < staff_datejoin);

-- STAFF_EXPERTISE
-------- 5 loai: Gym, Yoga, Boxing, Aerobic, Muay Thai hoac rong
ALTER TABLE STAFF
ADD CHECK (STAFF_EXPERTISE IN ('Gym', 'Yoga', 'Boxing', 'Aerobic', 'Muay Thai', null));

------------ PRODUCT ---------------
-- PRODUCT_TYPE
-------- 2 loai: Dụng cụ luyện tập, Thực phẩm bổ sung
ALTER TABLE PRODUCT
ADD CHECK (PRODUCT_TYPE IN ('Dụng cụ luyện tập', 'Thực phẩm bổ sung'));

-------- SL > 0
ALTER TABLE PRODUCT
ADD CHECK (PRODUCT_AMOUNT >= 0);

------------ DISCOUNT ---------------
--------- Phan tram giam gia: <= 25
ALTER TABLE DISCOUNT
ADD CHECK (DIS_PERCENT <= 25);

------------ PAYMENT_DETAIL ---------------
ALTER TABLE PAYMENT_DETAIL
ADD CHECK (AMOUNT > 0);

-------------------------------
---------- PROCEDURE ----------
-------------------------------


------------------------------------------
------ PROCEDURE INSERT THONG TIN --------
------------------------------------------

--Procedure INSERT CUSTOMER
CREATE OR REPLACE PROCEDURE Insert_CUSTOMER(v_name IN customer.cus_name%type, 
                                            v_birthday IN customer.cus_birthday%type, 
                                            v_gender IN customer.cus_gender%type, 
                                            v_password IN customer.cus_password%type, 
                                            v_datejoin IN customer.cus_datejoin%type, 
                                            v_address IN customer.cus_address%type, 
                                            v_telephone IN customer.cus_telephone%type, 
                                            v_memexpired IN customer.cus_memexpired%type) 
IS
BEGIN
    INSERT INTO CUSTOMER (cus_id, cus_name, cus_birthday, cus_gender, cus_password, 
                        cus_datejoin, cus_address, cus_telephone, cus_memexpired) 
    VALUES (CUSTOMER_SEQ.nextval, v_name, v_birthday, v_gender, v_password, 
            v_datejoin, v_address, v_telephone, v_memexpired);
    COMMIT;
END;

SELECT * FROM CUSTOMER;

BEGIN
    Insert_CUSTOMER('HUỲNH THỊ KIM CÚC', '02/04/1969', 'Nữ', 'JHUWGLEQ', '14/03/2022', 'TPHCM', '0342113781', NULL);
    Insert_CUSTOMER('ĐỖ THỊ DIỆU HUỆ', '08/02/1972', 'Nữ', 'LNJLYGOM', '15/03/2022', 'TPHCM', '0378865136', NULL);
    Insert_CUSTOMER('HUỲNH THỊ LẠI', '12/07/1969', 'Nữ', 'HXTDKRKC', '16/03/2022', 'TPHCM', '0879843435', NULL);
    Insert_CUSTOMER('HỒ QUANG DŨNG', '25/10/1969', 'Nam', 'AIFZOPNV', '17/03/2022', 'TPHCM', '0324558604', NULL);
    Insert_CUSTOMER('HUỲNH VĂN SƠN', '05/08/1972', 'Nam', 'QUTPZYAF', '18/03/2022', 'TPHCM', '0543677650', NULL);
    Insert_CUSTOMER('TRẦN ĐÌNH BỬU', '15/04/1963', 'Nam', 'KPNHOBCN', '21/03/2022', 'TPHCM', '0664753981', NULL);
    Insert_CUSTOMER('TRẦN THỊ BÍCH HOA', '06/12/1969', 'Nữ', 'YQWZBHLA', '22/03/2022', 'TPHCM', '0866424216', NULL);
    Insert_CUSTOMER('ĐOÀN THỊ KIM LIÊN', '01/01/1972', 'Nữ', 'BOQDNXQE', '23/03/2022', 'TPHCM', '0932346855', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN NGUYÊN', '15/12/1969', 'Nam', 'LHZXVXHK', '24/03/2022', 'TPHCM', '0756216570', NULL);
    Insert_CUSTOMER('HUỲNH NGỌC SƠN', '10/11/1962', 'Nam', 'QDJYFWUB', '25/03/2022', 'TPHCM', '0867234552', NULL);
    Insert_CUSTOMER('LÊ THÀNH ÂN', '01/01/1963', 'Nam', 'DYQWZCAU', '28/03/2022', 'TPHCM', '0876544771', NULL);
    Insert_CUSTOMER('LÊ THỊ SƯƠNG', '07/10/1966', 'Nữ', 'ZDFXBAWN', '29/03/2022', 'TPHCM', '0346775999', NULL);
    Insert_CUSTOMER('VÕ VĂN DŨNG', '28/08/1972', 'Nam', 'MXQRMHBZ', '30/03/2022', 'TPHCM', '0975342120', NULL);
    Insert_CUSTOMER('BÙI THANH LOAN', '17/06/1961', 'Nam', 'LELBOLRE', '31/03/2022', 'TPHCM', '0985675347', NULL);
    Insert_CUSTOMER('VÕ THỊ SƯƠNG', '25/02/1969', 'Nữ', 'CUZNCOJJ', '14/03/2022', 'TPHCM', '0675422438', NULL);
    Insert_CUSTOMER('LÊ THỊ BẢY', '26/10/1972', 'Nữ', 'ULZMVWTW', '15/03/2022', 'TPHCM', '0674665703', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ ĐÀO', '06/02/1964', 'Nữ', 'HZPMHDWJ', '16/03/2022', 'TPHCM', '0754342241', NULL);
    Insert_CUSTOMER('ĐỖ TRỌNG LƯƠNG', '18/04/1963', 'Nam', 'IYSZZDEE', '17/03/2022', 'TPHCM', '0375967905', NULL);
    Insert_CUSTOMER('PHẠM THỊ LIÊN', '10/10/1963', 'Nữ', 'JJUKKFXH', '18/03/2022', 'TPHCM', '0865445240', NULL);
    Insert_CUSTOMER('LÊ VĂN THÍCH', '20/06/1965', 'Nam', 'PALRCJKJ', '21/03/2022', 'TPHCM', '0866875670', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ TÁM', '17/08/1968', 'Nữ', 'MMPONUKX', '22/03/2022', 'TPHCM', '0676442447', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ NĂM', '20/05/1967', 'Nữ', 'RYVJGMJT', '23/03/2022', 'TPHCM', '0988877540', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ KIM DUNG', '02/09/1961', 'Nữ', 'PDUDIYWM', '24/03/2022', 'TPHCM', '0977856339', NULL);
    Insert_CUSTOMER('PHẠM THỊ SƯƠNG', '20/08/1972', 'Nữ', 'LTGFGYAT', '25/03/2022', 'TPHCM', '0787565532', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN CÓ', '28/02/1969', 'Nam', 'HUGCAHFJ', '28/03/2022', 'TPHCM', '0877543424', NULL);
    Insert_CUSTOMER('LÊ VĂN NGHỊ', '09/10/1966', 'Nam', 'OLROHZBR', '29/03/2022', 'TPHCM', '0358754636', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ MAY', '06/07/1963', 'Nữ', 'NMZWYQLD', '30/03/2022', 'TPHCM', '0923457553', NULL);
    Insert_CUSTOMER('VÕ ĐÌNH TIẾN', '21/12/1965', 'Nam', 'RLIWLWAI', '31/03/2022', 'TPHCM', '0988332456', NULL);
    Insert_CUSTOMER('NGUYỄN ĐÌNH THẾ', '28/02/1968', 'Nam', 'NCXZPCNL', '14/03/2022', 'TPHCM', '0988667560', NULL);
    Insert_CUSTOMER('VÕ VĂN VINH', '02/10/1972', 'Nam', 'EJPCGJSX', '15/03/2022', 'TPHCM', '0977564443', NULL);
    Insert_CUSTOMER('VÕ THỊ ĐẠO', '22/11/1961', 'Nữ', 'DRVMDNWA', '16/03/2022', 'TPHCM', '0973219083', NULL);
    Insert_CUSTOMER('TRẦN NGỌC ANH', '25/05/1972', 'Nam', 'PIGVNYLB', '17/03/2022', 'TPHCM', '0977210994', NULL);
    Insert_CUSTOMER('TRẦN THỊ SÁU', '02/03/1970', 'Nữ', 'HZOOJYME', '18/03/2022', 'TPHCM', '0834502225', NULL);
    Insert_CUSTOMER('HUỲNH VĂN TOÀN', '18/09/1971', 'Nam', 'KNVSDABQ', '21/03/2022', 'TPHCM', '0987801999', NULL);
    Insert_CUSTOMER('TRẦN THỊ THẢO', '19/06/1972', 'Nữ', 'QFVOHRXM', '22/03/2022', 'TPHCM', '0929875554', NULL);
    Insert_CUSTOMER('PHẠM THÀNH SƠN', '09/02/1962', 'Nam', 'OXYRITFF', '23/03/2022', 'TPHCM', '0370985149', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ THÁI', '10/09/1971', 'Nữ', 'GMKXHVXC', '24/03/2022', 'TPHCM', '0973459009', NULL);
    Insert_CUSTOMER('UNG VĂN PHÚ', '13/06/1963', 'Nam', 'GGLHZBBO', '25/03/2022', 'TPHCM', '0956663718', NULL);
    Insert_CUSTOMER('VÕ THỊ BÔNG', '18/03/1971', 'Nữ', 'ZXZCADUI', '28/03/2022', 'TPHCM', '0975873604', NULL);
    Insert_CUSTOMER('ĐẶNG THỊ THOA', '04/04/1967', 'Nữ', 'BXFPNQGN', '29/03/2022', 'TPHCM', '0943116780', NULL);
    Insert_CUSTOMER('LÊ ĐÌNH TRUNG', '15/05/1972', 'Nam', 'TNZEMYWJ', '30/03/2022', 'TPHCM', '0298800370', NULL);
    Insert_CUSTOMER('NGUYỄN NGỌC ANH', '09/02/1961', 'Nam', 'PKZQINBJ', '31/03/2022', 'TPHCM', '0987803452', NULL);
    Insert_CUSTOMER('HỒ VĂN RỒM', '23/12/1969', 'Nam', 'WIQVDGIC', '14/03/2022', 'TPHCM', '0953457118', NULL);
    Insert_CUSTOMER('NGUYỄN ĐÌNH THỦY', '01/12/1970', 'Nam', 'WCIHYLCT', '15/03/2022', 'TPHCM', '0948567443', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN HIẾU', '01/01/1969', 'Nam', 'CEKDTFBT', '16/03/2022', 'TPHCM', '0167908119', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN HÀ', '22/12/1965', 'Nam', 'IVRJYVDS', '17/03/2022', 'TPHCM', '0197670227', NULL);
    Insert_CUSTOMER('PHẠM THỊ THÚY NGA', '01/12/1968', 'Nữ', 'FRREQUMY', '18/03/2022', 'TPHCM', '0979805878', NULL);
    Insert_CUSTOMER('LÊ TRỌNG NAM', '05/11/1969', 'Nam', 'KBPCIBFH', '21/03/2022', 'TPHCM', '0944978022', NULL);
    Insert_CUSTOMER('LÊ THỊ THANH MINH', '25/12/1962', 'Nữ', 'PQGFPJJU', '22/03/2022', 'TPHCM', '0934567936', NULL);
    Insert_CUSTOMER('TRẦN THÁI HẢI', '02/08/1966', 'Nam', 'TLWGFDFM', '23/03/2022', 'TPHCM', '0977349031', NULL);
    Insert_CUSTOMER('TRẦN THỊ HẠNH', '02/04/1964', 'Nữ', 'XVPZXLTF', '24/03/2022', 'TPHCM', '0973457778', NULL);
    Insert_CUSTOMER('NGUYỄN NGỌC THÀNH', '24/06/1967', 'Nam', 'SVZJJWHO', '25/03/2022', 'TPHCM', '0973458996', NULL);
    Insert_CUSTOMER('LÊ XUÂN THỌ', '23/03/1967', 'Nam', 'UIPMEYCE', '28/03/2022', 'TPHCM', '0978553926', NULL);
    Insert_CUSTOMER('MAI THỊ NGỌC HÀ', '15/11/1968', 'Nữ', 'IBJABHRO', '29/03/2022', 'TPHCM', '0978553986', NULL);
    Insert_CUSTOMER('VÕ THỊ TRÂM', '16/12/1963', 'Nữ', 'OUZBWLGK', '30/03/2022', 'TPHCM', '0976167446', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN ĐỊNH', '25/10/1972', 'Nam', 'QMNEMJGZ', '31/03/2022', 'TPHCM', '0976157454', NULL);
    Insert_CUSTOMER('TRẦN THỊ KIM NGÂN', '05/07/1967', 'Nữ', 'PHKGJTIR', '14/03/2022', 'TPHCM', '0976157461', NULL);
    Insert_CUSTOMER('HUỲNH THỊ NON', '04/02/1969', 'Nữ', 'SXJSPZHJ', '15/03/2022', 'TPHCM', '0973407880', NULL);
    Insert_CUSTOMER('PHAN NGỌC QUY', '25/10/1968', 'Nam', 'DIVVVYZP', '16/03/2022', 'TPHCM', '0976113243', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN VINH', '08/09/1971', 'Nam', 'BGHUNVBE', '17/03/2022', 'TPHCM', '0973456883', NULL);
    Insert_CUSTOMER('HỒ THỊ NỮU', '02/08/1965', 'Nữ', 'ALJXDGLS', '18/03/2022', 'TPHCM', '0971234567', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN LẮM', '01/01/1964', 'Nam', 'AQTUROOD', '21/03/2022', 'TPHCM', '0972931115', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN KỲ', '21/03/1970', 'Nam', 'HXCPIKUZ', '22/03/2022', 'TPHCM', '0987220028', NULL);
    Insert_CUSTOMER('VÕ THỊ THI', '22/05/1967', 'Nữ', 'BSVRVCPY', '23/03/2022', 'TPHCM', '0928776555', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN HẦU', '02/03/1967', 'Nam', 'DVNDSBDE', '24/03/2022', 'TPHCM', '0986557113', NULL);
    Insert_CUSTOMER('ĐÀO THANH LỢI', '14/04/1971', 'Nam', 'IDVEBRYB', '25/03/2022', 'TPHCM', '0789667220', NULL);
    Insert_CUSTOMER('HUỲNH CẨM NHUNG', '14/11/1966', 'Nữ', 'OYKQJKIW', '28/03/2022', 'TPHCM', '0974529112', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ NGA', '12/08/1964', 'Nữ', 'TSZAFRAF', '29/03/2022', 'TPHCM', '0954667116', NULL);
    Insert_CUSTOMER('NGUYỄN THÀNH ĐƯỢC', '06/02/1965', 'Nam', 'JNJHUTIE', '30/03/2022', 'TPHCM', '0983447112', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ CHẺO', '20/05/1965', 'Nữ', 'UPSQIHLH', '31/03/2022', 'TPHCM', '0976559667', NULL);
    Insert_CUSTOMER('PHẠM THỊ LẠI', '12/04/1964', 'Nữ', 'JPGOQHIF', '14/03/2022', 'TPHCM', '0987554119', NULL);
    Insert_CUSTOMER('NGUYỄN ĐÌNH VĨNH', '04/08/1961', 'Nam', 'NWPMXRTJ', '15/03/2022', 'TPHCM', '0972336710', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ THUẬN', '18/12/1971', 'Nữ', 'FJKDMAGS', '16/03/2022', 'TPHCM', '0951116767', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ HỒNG NGA', '01/01/1963', 'Nữ', 'XCRSIGPU', '17/03/2022', 'TPHCM', '0987116544', NULL);
    Insert_CUSTOMER('NGUYỄN ĐỨC CƯỜNG', '17/01/1970', 'Nam', 'LEKNFYKG', '18/03/2022', 'TPHCM', '0987115432', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ ĐẸP', '20/08/1970', 'Nữ', 'TXCEFBWD', '21/03/2022', 'TPHCM', '0972356449', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ KIM LOAN', '03/07/1970', 'Nữ', 'QZUJKQFY', '22/03/2022', 'TPHCM', '0398266117', NULL);
    Insert_CUSTOMER('NGUYỄN ĐÌNH LÂN', '20/04/1970', 'Nam', 'VOSZXZSA', '23/03/2022', 'TPHCM', '0946115553', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ CÒN', '05/06/1969', 'Nữ', 'RYKFLHCT', '24/03/2022', 'TPHCM', '0978286891', NULL);
    Insert_CUSTOMER('HUỲNH VĂN THỪA', '09/06/1967', 'Nam', 'CVWCOHEI', '25/03/2022', 'TPHCM', '0953337662', NULL);
    Insert_CUSTOMER('PHẠM VĂN KỲ', '02/12/1963', 'Nam', 'BYZPRQJI', '28/03/2022', 'TPHCM', '0972118907', NULL);
    Insert_CUSTOMER('TRẦN NGỌC RẠNG', '01/02/1968', 'Nam', 'PNUZFCUV', '29/03/2022', 'TPHCM', '0964226117', NULL);
    Insert_CUSTOMER('TRẦN VĂN ANH', '16/11/1966', 'Nam', 'IOMNDGYW', '30/03/2022', 'TPHCM', '0974556768', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ THÔNG', '02/08/1971', 'Nữ', 'NYDNYXXK', '31/03/2022', 'TPHCM', '0974115759', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN HIỆP', '10/07/1969', 'Nam', 'GARGWZBT', '14/03/2022', 'TPHCM', '0976554228', NULL);
    Insert_CUSTOMER('TRẦN THỊ THÚY', '10/03/1970', 'Nữ', 'LFPXUPWG', '15/03/2022', 'TPHCM', '0948237662', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN SẮT', '10/02/1968', 'Nam', 'ZXGVZIYL', '16/03/2022', 'TPHCM', '0954667114', NULL);
    Insert_CUSTOMER('ĐẶNG THỊ KIM CẢNH', '01/01/1966', 'Nữ', 'AABEIVYZ', '17/03/2022', 'TPHCM', '0965557980', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ KIM LIÊN', '01/06/1972', 'Nữ', 'ZHNAOJGI', '18/03/2022', 'TPHCM', '0954117262', NULL);
    Insert_CUSTOMER('NGUYỄN ĐÌNH THỨC', '06/04/1967', 'Nam', 'ILMSDBZV', '21/03/2022', 'TPHCM', '0567803776', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ MẸO', '26/12/1965', 'Nữ', 'MSEQXPQU', '22/03/2022', 'TPHCM', '0976037873', NULL);
    Insert_CUSTOMER('TRẦN MINH THỐNG', '02/01/1965', 'Nam', 'YQSCOENF', '23/03/2022', 'TPHCM', '0341820739', NULL);
    Insert_CUSTOMER('NGUYỄN VĂN ÁI', '02/07/1969', 'Nam', 'GCDXQDNR', '24/03/2022', 'TPHCM', '0674211632', NULL);
    Insert_CUSTOMER('TRẦN THỊ GÁI', '20/10/1972', 'Nữ', 'LBUGEKDE', '25/03/2022', 'TPHCM', '0976557111', NULL);
    Insert_CUSTOMER('LÊ VĂN NGHĨA', '03/08/1962', 'Nam', 'AMWSABCN', '28/03/2022', 'TPHCM', '0846778262', NULL);
    Insert_CUSTOMER('HỒ THỊ QUEN', '08/05/1964', 'Nữ', 'KQDAWQMP', '29/03/2022', 'TPHCM', '0902511887', NULL);
    Insert_CUSTOMER('NGUYỄN ÂU', '07/05/1962', 'Nam', 'YZHSFFCH', '30/03/2022', 'TPHCM', '0974558110', NULL);
    Insert_CUSTOMER('NGUYỄN THỊ THU', '08/07/1969', 'Nữ', 'ATZYIRKS', '31/03/2022', 'TPHCM', '0974562227', NULL);
    Insert_CUSTOMER('NGUYỄN KIM HÀ', '09/10/1969', 'Nữ', 'ALVCJVUX', '14/03/2022', 'TPHCM', '0162337668', NULL);
    Insert_CUSTOMER('TRƯƠNG THỊ THỦY', '21/03/1972', 'Nữ', 'CYMVEZGT', '15/03/2022', 'TPHCM', '0457892614', NULL);
END;

--Procedure INSERT STAFF
CREATE OR REPLACE PROCEDURE Insert_STAFF(v_name IN staff.staff_name%type, v_birthday IN staff.staff_birthday%type, 
                                        v_gender IN staff.staff_gender%type, v_type IN staff.staff_type%type, 
                                        v_datejoin IN staff.staff_datejoin%type, v_address IN staff.staff_address%type, 
                                        v_telephone IN staff.staff_telephone%type, v_password IN staff.staff_password%type, 
                                        v_salary IN staff.staff_salary%type, v_experience IN staff.staff_experience%type,
                                        v_expertise IN staff.staff_expertise%type)
IS
BEGIN
  INSERT INTO STAFF (staff_id, staff_name, staff_birthday, staff_gender, staff_type, 
                        staff_datejoin, staff_address, staff_telephone, staff_password, 
                        staff_salary, staff_experience, staff_expertise) 
  VALUES (STAFF_SEQ.nextval, v_name, v_birthday, v_gender, v_type, v_datejoin, 
            v_address, v_telephone, v_password, v_salary, v_experience, v_expertise);
  COMMIT;
END;

SELECT * FROM STAFF;
BEGIN
    Insert_STAFF('NGUYỄN VĂN MƯỜI', '02/04/1969', 'Nam', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0990502101', 'FIJXQKDD', '15000000', '3', 'Yoga');
    Insert_STAFF('HUỲNH VĂN BỐN', '10/06/1966', 'Nam', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0959283402', 'XPCUDGQP', '15000000', '3', 'Yoga');
    Insert_STAFF('DƯƠNG THỊ YẾN', '04/04/1968', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0916638503', 'HQKYCPHJ', '15000000', '3', 'Gym');
    Insert_STAFF('NGUYỄN THỊ BẢY', '19/10/1972', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0987027704', 'HUDPLEYN', '15000000', '4', 'Muay Thai');
    Insert_STAFF('NGUYỄN VĂN LÍA', '16/06/1966', 'Nam', 'Huấn luyện viên', '08/10/2021', 'TPHCM', '0947234705', 'IVCMNRSU', '15000000', '4', 'Boxing');
    Insert_STAFF('TRƯƠNG VĂN ĐÔNG', '12/04/1967', 'Nam', 'Huấn luyện viên', '08/10/2021', 'TPHCM', '0983994206', 'MMQHOZKA', '15000000', '4', 'Boxing');
    Insert_STAFF('NGUYỄN ĐẪU', '08/01/1967', 'Nam', 'Huấn luyện viên', '08/10/2021', 'TPHCM', '0986485307', 'BGLYWSOX', '15000000', '3', 'Boxing');
    Insert_STAFF('TRẦN THỊ HÒA', '09/07/1970', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0914876508', 'NYKASEEI', '15000000', '4', 'Gym');
    Insert_STAFF('TRẦN VIẾT PHỤNG', '10/05/1972', 'Nam', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0917793909', 'SGKJHNNR', '15000000', '5', 'Boxing');
    Insert_STAFF('LÊ THỊ LỆ HOA', '25/05/1969', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0960274710', 'KQMMBPRM', '15000000', '5', 'Boxing');
    Insert_STAFF('VÕ KIM LINH', '04/03/1967', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0983220011', 'JFEOTMRG', '15000000', '5', 'Yoga');
    Insert_STAFF('LÊ VĂN AN', '01/01/1968', 'Nam', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0992353312', 'FJYJHCUT', '15000000', '4', 'Gym');
    Insert_STAFF('NGUYỄN NHƯ PHƯỚC', '01/11/1966', 'Nam', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0954438913', 'MYLEXGKQ', '15000000', '1', 'Boxing');
    Insert_STAFF('VÕ MINH HẢI', '05/01/1970', 'Nam', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0999021914', 'ECKRXRPK', '15000000', '2', 'Yoga');
    Insert_STAFF('TRẦN THỊ THU', '19/04/1964', 'Nữ', 'Huấn luyện viên', '08/10/2021', 'TPHCM', '0941593415', 'OAMYBXFV', '15000000', '2', 'Muay Thai');
    Insert_STAFF('NGUYỄN VĂN NY', '11/06/1960', 'Nam', 'Huấn luyện viên', '08/10/2021', 'TPHCM', '0917238916', 'ONRCCBTZ', '15000000', '2', 'Yoga');
    Insert_STAFF('ĐOÀN VĂN TÂM', '27/06/1967', 'Nam', 'Huấn luyện viên', '08/10/2021', 'TPHCM', '0982412017', 'ZDZGFPKT', '15000000', '3', 'Muay Thai');
    Insert_STAFF('LÊ THỊ HƯƠNG', '10/04/1967', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0905797318', 'SFTRYCUK', '15000000', '3', 'Yoga');
    Insert_STAFF('HUỲNH THỊ ĐUA', '20/05/1966', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0941071919', 'OZEYMFLN', '15000000', '5', 'Gym');
    Insert_STAFF('BÙI THỊ HUYỀN', '20/02/1968', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0907934620', 'NSRBJMJI', '15000000', '2', 'Yoga');
    Insert_STAFF('HUỲNH THỊ TUYẾT', '01/01/1968', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0941630621', 'TNZXIDOM', '15000000', '1', 'Yoga');
    Insert_STAFF('VÕ THỊ KIM HUỆ', '26/06/1965', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0914014822', 'WLNJCQUY', '15000000', '4', 'Muay Thai');
    Insert_STAFF('NGUYỄN THỊ PHỤNG', '20/12/1967', 'Nữ', 'Huấn luyện viên', '07/10/2021', 'TPHCM', '0985366123', 'HNXGHMCS', '15000000', '3', 'Aerobic');
    Insert_STAFF('PHẠM THỊ MƯNG', '10/02/1967', 'Nữ', 'Thu ngân', '07/10/2021', 'TPHCM', '0983296824', 'KYRDUUNV', '10000000', '4', null);
    Insert_STAFF('PHÙNG VĂN TÁM', '04/08/1966', 'Nam', 'Thu ngân', '08/10/2021', 'TPHCM', '0947903725', 'EKEIYRRQ', '10000000', '2', null);
    Insert_STAFF('NGUYỄN THỊ XƯA', '20/08/1965', 'Nữ', 'Thu ngân', '08/10/2021', 'TPHCM', '0964259326', 'UHDTRJEF', '10000000', '5', null);
    Insert_STAFF('NGUYỄN THỊ KIỆP', '07/06/1967', 'Nữ', 'Thu ngân', '08/10/2021', 'TPHCM', '0994207227', 'RSGOFFPO', '10000000', '3', null);
    Insert_STAFF('UNG THỊ HUỆ', '30/07/1965', 'Nữ', 'Thu ngân', '07/10/2021', 'TPHCM', '0977739828', 'UXTSZAJP', '10000000', '4', null);
    Insert_STAFF('NGUYỄN THỊ SÁU THỊ SÁU', '16/12/1968', 'Nữ', 'Quản lý', '07/10/2021', 'TPHCM', '0941664529', 'GWAJCOJU', '20000000', '4', null);
    Insert_STAFF('LÊ THỊ BÍCH HƯƠNG', '04/08/1971', 'Nữ', 'Quản lý', '07/10/2021', 'TPHCM', '0951465930', 'OXTUMFPH', '20000000', '5', null);
END;

--Procedure INSERT PRODUCT
CREATE OR REPLACE PROCEDURE Insert_PRODUCT(v_name IN product.product_name%type, v_price IN product.product_price%type, 
                                        v_type IN product.product_type%type, v_amount IN product.product_amount%type)
IS
BEGIN
    INSERT INTO PRODUCT (product_id, product_name, product_price, product_type, product_amount) 
    VALUES (PRODUCT_SEQ.nextval, v_name, v_price, v_type, v_amount);
    COMMIT;
END;

SELECT * FROM PRODUCT;
BEGIN
    Insert_PRODUCT('Túi Tập Gym Nhỏ Adidas', 120000, 'Dụng cụ luyện tập', 26);
    Insert_PRODUCT('Găng tay tập gym kết hợp bảo vệ cổ tay chất liệu Silicon', 100000, 'Dụng cụ luyện tập', 40);
    Insert_PRODUCT('Đệm vai Squat , Hip Thrust BarPad APHM', 250000, 'Dụng cụ luyện tập', 20);
    Insert_PRODUCT('Quấn gối Aolikes - Quấn và Bảo vệ đầu gối tập thể thao', 80000, 'Dụng cụ luyện tập', 30);
    Insert_PRODUCT('Găng tay hở ngón thoáng khí chống trượt', 50000, 'Dụng cụ luyện tập', 22);
    Insert_PRODUCT('Tay Cầm Hỗ Trợ Tập Gym Tiện Dụng', 30000, 'Dụng cụ luyện tập', 23);
    Insert_PRODUCT('Tấm đệm đầu gối T3VN bằng TPE', 20000, 'Dụng cụ luyện tập', 21);
    Insert_PRODUCT('Tấm đệm đầu gối T3VN bằng TPE', 50000, 'Dụng cụ luyện tập', 30);
    Insert_PRODUCT('Băng quấn bảo vệ đầu gối hỗ trợ tập thể thao', 40000, 'Dụng cụ luyện tập', 32);
    Insert_PRODUCT('Quấn Cổ Tay Aolikes', 80000, 'Dụng cụ luyện tập', 37);
    Insert_PRODUCT('Găng tay thể thao hở ngón', 80000, 'Dụng cụ luyện tập', 28);
    Insert_PRODUCT('Dây Cáp Tricep Rope Bicep Hỗ Trợ Tập GYM', 250000, 'Dụng cụ luyện tập', 26);
    Insert_PRODUCT('Ống bó gối hỗ trợ tập thể thao', 40000, 'Dụng cụ luyện tập', 22);
    Insert_PRODUCT('Aolikes 1 Cặp Dây Đai Kháng Lực Chữ D', 80000, 'Dụng cụ luyện tập', 39);
    Insert_PRODUCT('Aolikes 1 Đai Quấn Co Giãn Hỗ Trợ Khuỷu Tay Tập Gym', 90000, 'Dụng cụ luyện tập', 34);
    Insert_PRODUCT('Găng Tay Sport Cụt Ngón Tập Gym Mẫu Mới Lót Nhung', 70000, 'Dụng cụ luyện tập', 33);
    Insert_PRODUCT('Dây Kháng Lực Tập Yoga', 80000, 'Dụng cụ luyện tập', 21);
    Insert_PRODUCT('1 cặp Dây kéo lưng Phụ Kiện thể thao Gym Aolikes', 90000, 'Dụng cụ luyện tập', 40);
    Insert_PRODUCT('Găng Tay Tập Gym, Bao Tay Nâng Tạ Tập Tạ Nữ Cao Cấp', 100000, 'Dụng cụ luyện tập', 33);
    Insert_PRODUCT('Dây Kháng Lực Tập Yoga', 70000, 'Dụng cụ luyện tập', 20);
    Insert_PRODUCT('1 KG WHEY PROTEIN CONCENTRATE 80% NZMP - Tăng Cơ Giảm Mỡ', 200000, 'Thực phẩm bổ sung', 32);
    Insert_PRODUCT('Combo viên uống hỗ trợ sức khoẻ, tăng cơ dành cho người mới tập gym', 1700000, 'Thực phẩm bổ sung', 22);
    Insert_PRODUCT('Combo viên uống hỗ trợ sức khoẻ, tăng cơ dành cho người mới tập gym', 650000, 'Thực phẩm bổ sung', 27);
    Insert_PRODUCT('Biotech Vegan Protein Sample', 50000, 'Thực phẩm bổ sung', 24);
    Insert_PRODUCT('VITAMIN 3B bổ sung vitamin nhóm B', 60000, 'Thực phẩm bổ sung', 31);
    Insert_PRODUCT('COMBO PHÁT TRIỂN CƠ BẮP, TĂNG SỨC BỀN CHO NGƯỜI TẬP GYM', 2600000, 'Thực phẩm bổ sung', 31);
END;

--Procedure INSERT DISCOUNT
CREATE OR REPLACE PROCEDURE Insert_DISCOUNT(v_code IN discount.dis_code%type, v_percent IN discount.dis_percent%type)
IS
BEGIN
    INSERT INTO DISCOUNT (dis_id, dis_code, dis_percent) 
    VALUES (DISCOUNT_SEQ.nextval, v_code, v_percent);
    COMMIT;
END;

SELECT * FROM DISCOUNT;
BEGIN
    INSERT_DISCOUNT('D1ZYJRQ301', 5);
    INSERT_DISCOUNT('G8KDUAF102', 5);
    INSERT_DISCOUNT('V0TSMCZ203', 5);
    INSERT_DISCOUNT('J1VVIWK104', 5);
    INSERT_DISCOUNT('F9LWSJP505', 5);
    INSERT_DISCOUNT('D6CMEMN006', 5);
    INSERT_DISCOUNT('G2ZZEKR407', 5);
    INSERT_DISCOUNT('U2FSRHK208', 5);
    INSERT_DISCOUNT('P9QOPUH709', 5);
    INSERT_DISCOUNT('G2DPNUX010', 5);
    INSERT_DISCOUNT('G9ZQOTX711', 5);
    INSERT_DISCOUNT('X9IWMTW112', 5);
    INSERT_DISCOUNT('K5GROZZ013', 5);
    INSERT_DISCOUNT('V9CYECL514', 5);
    INSERT_DISCOUNT('G5KULNW915', 5);
    INSERT_DISCOUNT('Q0QJPEP416', 5);
    INSERT_DISCOUNT('O3CIWHC617', 5);
    INSERT_DISCOUNT('P6FJWQZ418', 5);
    INSERT_DISCOUNT('X2WGXHU819', 5);
    INSERT_DISCOUNT('O3XSFOI320', 5);
    INSERT_DISCOUNT('J9MWNWZ121', 5);
    INSERT_DISCOUNT('N3XUJFO422', 5);
    INSERT_DISCOUNT('Z5BKBPC123', 5);
    INSERT_DISCOUNT('G9QGFQJ224', 5);
    INSERT_DISCOUNT('F3KIKZK725', 5);
    INSERT_DISCOUNT('A5UTEGW526', 5);
    INSERT_DISCOUNT('W3RBWYO027', 5);
    INSERT_DISCOUNT('A1OJMAB528', 5);
    INSERT_DISCOUNT('T0WWDJZ629', 5);
    INSERT_DISCOUNT('D9YJGSR730', 5);
    INSERT_DISCOUNT('M2PHIFL531', 5);
    INSERT_DISCOUNT('X7TSCQP832', 5);
    INSERT_DISCOUNT('A0HJDYH133', 5);
    INSERT_DISCOUNT('V9JAYBW334', 5);
    INSERT_DISCOUNT('I1QLMUP135', 5);
    INSERT_DISCOUNT('Q9GRITJ836', 5);
    INSERT_DISCOUNT('L4OHKSD237', 5);
    INSERT_DISCOUNT('H7RYUKJ238', 5);
    INSERT_DISCOUNT('P2UVRFE639', 5);
    INSERT_DISCOUNT('E7NMKVP840', 5);
    INSERT_DISCOUNT('Y8KLTNG141', 5);
    INSERT_DISCOUNT('Q4JMUOL042', 5);
    INSERT_DISCOUNT('A8TXFWO243', 5);
    INSERT_DISCOUNT('I8QOQOG944', 5);
    INSERT_DISCOUNT('G0IEYKD445', 5);
    INSERT_DISCOUNT('F4MRBSY946', 5);
    INSERT_DISCOUNT('C6FASPD947', 5);
    INSERT_DISCOUNT('E5SMQNF448', 5);
    INSERT_DISCOUNT('C8OYGPX149', 5);
    INSERT_DISCOUNT('R7BGLUE650', 5);
    INSERT_DISCOUNT('B5WRPXD151', 10);
    INSERT_DISCOUNT('E5RYFVU352', 10);
    INSERT_DISCOUNT('Z4WVTHJ553', 10);
    INSERT_DISCOUNT('I6PONRA754', 10);
    INSERT_DISCOUNT('B9ZSKIB255', 10);
    INSERT_DISCOUNT('T6RAYQN856', 10);
    INSERT_DISCOUNT('B4YTTEZ057', 10);
    INSERT_DISCOUNT('F5BQHKM858', 10);
    INSERT_DISCOUNT('Z9DJCBX959', 10);
    INSERT_DISCOUNT('F9ACXZR960', 10);
    INSERT_DISCOUNT('A1CHIOQ761', 10);
    INSERT_DISCOUNT('H1FZPAY162', 10);
    INSERT_DISCOUNT('Z5BLHOB163', 10);
    INSERT_DISCOUNT('M0QNUIL264', 10);
    INSERT_DISCOUNT('U5HXDRL265', 10);
    INSERT_DISCOUNT('Y0MGCDY966', 10);
    INSERT_DISCOUNT('M0EHTLC867', 10);
    INSERT_DISCOUNT('A8JIHCI568', 10);
    INSERT_DISCOUNT('Q5HANEB769', 10);
    INSERT_DISCOUNT('G7BCAPA970', 10);
    INSERT_DISCOUNT('E9CXOLE171', 10);
    INSERT_DISCOUNT('X7CUHXW672', 10);
    INSERT_DISCOUNT('S7RWHWI773', 10);
    INSERT_DISCOUNT('A6PJMEG974', 10);
    INSERT_DISCOUNT('D3TOUGQ875', 10);
    INSERT_DISCOUNT('G1DPXNP376', 10);
    INSERT_DISCOUNT('C1IGGSX277', 10);
    INSERT_DISCOUNT('D0ZANHP878', 10);
    INSERT_DISCOUNT('G8YLBSC279', 10);
    INSERT_DISCOUNT('P2XFUAY480', 10);
    INSERT_DISCOUNT('F3HLUXB681', 10);
    INSERT_DISCOUNT('T4XSPEB982', 10);
    INSERT_DISCOUNT('R3OTNUB583', 10);
    INSERT_DISCOUNT('Q2LGZNW684', 10);
    INSERT_DISCOUNT('G8SOFQL185', 10);
    INSERT_DISCOUNT('F6TNIYL786', 10);
    INSERT_DISCOUNT('C1UIBWR887', 10);
    INSERT_DISCOUNT('Y4PABNL888', 10);
    INSERT_DISCOUNT('J3UPDTV589', 10);
    INSERT_DISCOUNT('P5PHSHL290', 20);
    INSERT_DISCOUNT('R7IMTJK091', 20);
    INSERT_DISCOUNT('C0KZCMS692', 20);
    INSERT_DISCOUNT('K2NINDS893', 20);
    INSERT_DISCOUNT('H6UCHAG794', 20);
    INSERT_DISCOUNT('K5GKECA195', 20);
    INSERT_DISCOUNT('W7ODPST496', 20);
    INSERT_DISCOUNT('D4WPIEV597', 20);
    INSERT_DISCOUNT('P1GFQKZ098', 20);
    INSERT_DISCOUNT('N6MXUGX299', 20);
END;

--Procedure INSERT ROOM
CREATE OR REPLACE PROCEDURE Insert_ROOM(v_capacity IN ROOM.room_capacity%TYPE)                
IS
BEGIN
    INSERT INTO ROOM (room_id, room_capacity)
    VALUES (ROOM_SEQ.nextval, v_capacity);
    COMMIT;
END;

SELECT * FROM ROOM;
BEGIN
    INSERT_ROOM(40);
    INSERT_ROOM(20);
    INSERT_ROOM(40);
    INSERT_ROOM(70);
    INSERT_ROOM(40);
    INSERT_ROOM(60);
    INSERT_ROOM(60);
    INSERT_ROOM(30);
    INSERT_ROOM(40);
    INSERT_ROOM(50);
    INSERT_ROOM(20);
    INSERT_ROOM(60);
    INSERT_ROOM(70);
    INSERT_ROOM(50);
    INSERT_ROOM(40);
    INSERT_ROOM(20);
    INSERT_ROOM(50);
    INSERT_ROOM(50);
    INSERT_ROOM(70);
    INSERT_ROOM(50);
END;

--Procedure INSERT CLASS
CREATE OR REPLACE PROCEDURE Insert_Class(v_period IN class.class_period%type, v_date IN class.class_date%type, 
                                        v_title IN class.class_title%type, v_cost IN class.class_cost%type,
                                        v_room IN class.room_id%type)                
IS
BEGIN
    INSERT INTO CLASS (class_id, class_period, class_date, class_title, class_cost, room_id) 
    VALUES (CLASS_SEQ.nextval, v_period, v_date, v_title, v_cost, v_room);
    COMMIT;
END;

SELECT * FROM CLASS;

BEGIN
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Gym cho nam giới' , 5000000, 1);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Gym cho nữ giới' , 5000000, 2);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Gym cho người mới' , 5000000, 3);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Giảm mỡ bụng' , 5000000, 4);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Giảm cân cấp tốc' , 5000000, 5);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Giảm mỡ toàn thân' , 5000000, 6);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Tăng chiều cao' , 5000000, 7);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Tăng cường cơ bắp' , 5000000, 8);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Phục hồi chức năng' , 5000000, 9);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Khôi phuc vóc dáng sau sinh' , 5000000, 10);
    INSERT_CLASS('5h30 - 7h00', '01/05/2022', 'Aerobic' , 5000000, 11);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Gym cho nam giới' , 5000000, 1);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Gym cho nữ giới' , 5000000, 2);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Gym cho người mới' , 5000000, 3);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Giảm mỡ bụng' , 5000000, 4);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Giảm cân cấp tốc' , 5000000, 5);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Giảm mỡ toàn thân' , 5000000, 6);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Tăng chiều cao' , 5000000, 7);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Tăng cường cơ bắp' , 5000000, 8);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Phục hồi chức năng' , 5000000, 9);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Khôi phuc vóc dáng sau sinh' , 5000000, 10);
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Aerobic' , 5000000, 11);
END;
    
BEGIN 
    INSERT_CLASS('5h30 - 7h00', '16/06/2022', 'Gym cho nam giới' , 5000000, 3);
END;
--Procedure INSERT STAFF_CLASS
CREATE OR REPLACE PROCEDURE Insert_STAFF_CLASS(v_class IN staff_class.class_id%type, v_staff IN staff_class.staff_id%type)
IS
BEGIN
    INSERT INTO STAFF_CLASS (class_id, staff_id) 
    VALUES (v_class, v_staff);
    COMMIT;
END;

SELECT * FROM STAFF_CLASS;
BEGIN
    INSERT_STAFF_CLASS(7, 1);
    INSERT_STAFF_CLASS(13, 2);
    INSERT_STAFF_CLASS(8, 3);
    INSERT_STAFF_CLASS(2, 4);
    INSERT_STAFF_CLASS(3, 5);
    INSERT_STAFF_CLASS(9, 6);
    INSERT_STAFF_CLASS(22, 7);
    INSERT_STAFF_CLASS(4, 8);
    INSERT_STAFF_CLASS(16, 9);
    INSERT_STAFF_CLASS(19, 11);
    INSERT_STAFF_CLASS(6, 12);
    INSERT_STAFF_CLASS(14, 13);
    INSERT_STAFF_CLASS(18, 14);
    INSERT_STAFF_CLASS(15, 15);
    INSERT_STAFF_CLASS(10, 16);
    INSERT_STAFF_CLASS(21, 17);
    INSERT_STAFF_CLASS(5, 18);
    INSERT_STAFF_CLASS(20, 19);
    INSERT_STAFF_CLASS(12, 20);
    INSERT_STAFF_CLASS(11, 21);
    INSERT_STAFF_CLASS(1, 22);
    INSERT_STAFF_CLASS(17, 10);
END;

--Procedure INSERT PAYMENT
CREATE OR REPLACE PROCEDURE Insert_PAYMENT(v_lastday IN payment.payment_lastday%type, v_mode IN payment.payment_mode%type, 
                                        v_staff IN payment.staff_id%type, v_customer IN payment.cus_id%type,
                                        v_discount IN payment.dis_id%type)                
IS
BEGIN
    INSERT INTO PAYMENT (payment_id, payment_lastday, payment_mode, staff_id, cus_id, dis_id) 
    VALUES (PAYMENT_SEQ.nextval, v_lastday, v_mode, v_staff, v_customer, v_discount);
    COMMIT;
END;

SELECT * FROM PAYMENT;
BEGIN
    INSERT_PAYMENT('14/03/2022', 'Tiền mặt', 24, 1,  1);
    INSERT_PAYMENT('15/03/2022', 'Tiền mặt', 24, 2,  null);
    INSERT_PAYMENT('16/03/2022', 'Tiền mặt', 24, 3,  null);
    INSERT_PAYMENT('17/03/2022', 'Thẻ tín dụng', 24, 4,  4);
    INSERT_PAYMENT('18/03/2022', 'Tiền mặt', 28, 5,  null);
    INSERT_PAYMENT('21/03/2022', 'Thẻ tín dụng', 24, 6,  null);
    INSERT_PAYMENT('22/03/2022', 'Tiền mặt', 25, 7,  7);
    INSERT_PAYMENT('23/03/2022', 'Tiền mặt', 24, 8,  null);
    INSERT_PAYMENT('24/03/2022', 'Tiền mặt', 24, 9,  9);
    INSERT_PAYMENT('25/03/2022', 'Tiền mặt', 27, 10,  null);
    INSERT_PAYMENT('28/03/2022', 'Tiền mặt', 24, 11,  11);
    INSERT_PAYMENT('29/03/2022', 'Thẻ tín dụng', 24, 12,  null);
    INSERT_PAYMENT('30/03/2022', 'Thẻ tín dụng', 24, 13,  null);
    INSERT_PAYMENT('31/03/2022', 'Thẻ tín dụng', 28, 14,  14);
    INSERT_PAYMENT('14/03/2022', 'Tiền mặt', 25, 15,  15);
    INSERT_PAYMENT('15/03/2022', 'Tiền mặt', 28, 16,  null);
    INSERT_PAYMENT('16/03/2022', 'Tiền mặt', 27, 17,  17);
    INSERT_PAYMENT('17/03/2022', 'Tiền mặt', 27, 18,  18);
    INSERT_PAYMENT('18/03/2022', 'Thẻ tín dụng', 25, 19,  19);
    INSERT_PAYMENT('21/03/2022', 'Thẻ tín dụng', 28, 20,  20);
    INSERT_PAYMENT('22/03/2022', 'Tiền mặt', 25, 21,  21);
    INSERT_PAYMENT('23/03/2022', 'Tiền mặt', 27, 22,  22);
    INSERT_PAYMENT('24/03/2022', 'Thẻ tín dụng', 25, 23,  null);
    INSERT_PAYMENT('25/03/2022', 'Thẻ tín dụng', 27, 24,  null);
    INSERT_PAYMENT('28/03/2022', 'Tiền mặt', 24, 25,  null);
    INSERT_PAYMENT('29/03/2022', 'Thẻ tín dụng', 27, 26,  null);
    INSERT_PAYMENT('30/03/2022', 'Tiền mặt', 26, 27,  27);
    INSERT_PAYMENT('31/03/2022', 'Thẻ tín dụng', 25, 28,  28);
    INSERT_PAYMENT('14/03/2022', 'Tiền mặt', 26, 29,  null);
    INSERT_PAYMENT('15/03/2022', 'Tiền mặt', 26, 30,  30);
    INSERT_PAYMENT('16/03/2022', 'Tiền mặt', 25, 31,  null);
    INSERT_PAYMENT('17/03/2022', 'Thẻ tín dụng', 26, 32,  32);
    INSERT_PAYMENT('18/03/2022', 'Thẻ tín dụng', 28, 33,  null);
    INSERT_PAYMENT('21/03/2022', 'Thẻ tín dụng', 24, 34,  34);
    INSERT_PAYMENT('22/03/2022', 'Thẻ tín dụng', 27, 35,  35);
    INSERT_PAYMENT('23/03/2022', 'Tiền mặt', 27, 36,  null);
    INSERT_PAYMENT('24/03/2022', 'Thẻ tín dụng', 24, 37,  37);
    INSERT_PAYMENT('25/03/2022', 'Thẻ tín dụng', 27, 38,  null);
    INSERT_PAYMENT('28/03/2022', 'Tiền mặt', 26, 39,  39);
    INSERT_PAYMENT('29/03/2022', 'Thẻ tín dụng', 27, 40,  null);
    INSERT_PAYMENT('30/03/2022', 'Thẻ tín dụng', 28, 41,  41);
    INSERT_PAYMENT('31/03/2022', 'Thẻ tín dụng', 27, 42,  null);
    INSERT_PAYMENT('14/03/2022', 'Tiền mặt', 25, 43,  43);
    INSERT_PAYMENT('15/03/2022', 'Tiền mặt', 26, 44,  44);
    INSERT_PAYMENT('16/03/2022', 'Tiền mặt', 25, 45,  null);
    INSERT_PAYMENT('17/03/2022', 'Thẻ tín dụng', 27, 46,  46);
    INSERT_PAYMENT('18/03/2022', 'Thẻ tín dụng', 27, 47,  null);
    INSERT_PAYMENT('21/03/2022', 'Thẻ tín dụng', 26, 48,  48);
    INSERT_PAYMENT('22/03/2022', 'Thẻ tín dụng', 27, 49,  null);
    INSERT_PAYMENT('23/03/2022', 'Tiền mặt', 28, 50,  null);
    INSERT_PAYMENT('24/03/2022', 'Thẻ tín dụng', 24, 51,  51);
    INSERT_PAYMENT('25/03/2022', 'Tiền mặt', 27, 52,  null);
    INSERT_PAYMENT('28/03/2022', 'Thẻ tín dụng', 24, 53,  53);
    INSERT_PAYMENT('29/03/2022', 'Thẻ tín dụng', 26, 54,  null);
    INSERT_PAYMENT('30/03/2022', 'Tiền mặt', 24, 55,  null);
    INSERT_PAYMENT('31/03/2022', 'Tiền mặt', 26, 56,  56);
    INSERT_PAYMENT('14/03/2022', 'Thẻ tín dụng', 24, 57,  57);
    INSERT_PAYMENT('15/03/2022', 'Tiền mặt', 27, 58,  58);
    INSERT_PAYMENT('16/03/2022', 'Thẻ tín dụng', 27, 59,  null);
    INSERT_PAYMENT('17/03/2022', 'Thẻ tín dụng', 27, 60,  60);
    INSERT_PAYMENT('18/03/2022', 'Thẻ tín dụng', 28, 61,  61);
    INSERT_PAYMENT('21/03/2022', 'Tiền mặt', 27, 62,  62);
    INSERT_PAYMENT('22/03/2022', 'Tiền mặt', 27, 63,  63);
    INSERT_PAYMENT('23/03/2022', 'Thẻ tín dụng', 26, 64,  null);
    INSERT_PAYMENT('24/03/2022', 'Tiền mặt', 27, 65,  null);
    INSERT_PAYMENT('25/03/2022', 'Thẻ tín dụng', 27, 66,  66);
    INSERT_PAYMENT('28/03/2022', 'Tiền mặt', 25, 67,  67);
    INSERT_PAYMENT('29/03/2022', 'Tiền mặt', 24, 68,  68);
    INSERT_PAYMENT('30/03/2022', 'Tiền mặt', 24, 69,  null);
    INSERT_PAYMENT('31/03/2022', 'Tiền mặt', 27, 70,  null);
    INSERT_PAYMENT('14/03/2022', 'Tiền mặt', 26, 71,  71);
    INSERT_PAYMENT('15/03/2022', 'Thẻ tín dụng', 25, 72,  null);
    INSERT_PAYMENT('16/03/2022', 'Tiền mặt', 28, 73,  null);
    INSERT_PAYMENT('17/03/2022', 'Tiền mặt', 28, 74,  74);
    INSERT_PAYMENT('18/03/2022', 'Thẻ tín dụng', 27, 75,  75);
    INSERT_PAYMENT('21/03/2022', 'Thẻ tín dụng', 26, 76,  76);
    INSERT_PAYMENT('22/03/2022', 'Tiền mặt', 26, 77,  77);
    INSERT_PAYMENT('23/03/2022', 'Thẻ tín dụng', 25, 78,  78);
    INSERT_PAYMENT('24/03/2022', 'Thẻ tín dụng', 25, 79,  79);
    INSERT_PAYMENT('25/03/2022', 'Thẻ tín dụng', 27, 80,  80);
    INSERT_PAYMENT('28/03/2022', 'Thẻ tín dụng', 25, 81,  81);
    INSERT_PAYMENT('29/03/2022', 'Tiền mặt', 24, 82,  82);
    INSERT_PAYMENT('30/03/2022', 'Thẻ tín dụng', 25, 83,  83);
    INSERT_PAYMENT('31/03/2022', 'Tiền mặt', 26, 84,  null);
    INSERT_PAYMENT('14/03/2022', 'Tiền mặt', 26, 85,  85);
    INSERT_PAYMENT('15/03/2022', 'Tiền mặt', 26, 86,  86);
    INSERT_PAYMENT('16/03/2022', 'Thẻ tín dụng', 28, 87,  87);
    INSERT_PAYMENT('17/03/2022', 'Thẻ tín dụng', 24, 88,  88);
    INSERT_PAYMENT('18/03/2022', 'Tiền mặt', 28, 89,  89);
    INSERT_PAYMENT('21/03/2022', 'Tiền mặt', 26, 90,  90);
    INSERT_PAYMENT('22/03/2022', 'Tiền mặt', 27, 91,  null);
    INSERT_PAYMENT('23/03/2022', 'Tiền mặt', 28, 92,  92);
    INSERT_PAYMENT('24/03/2022', 'Thẻ tín dụng', 24, 93,  null);
    INSERT_PAYMENT('25/03/2022', 'Thẻ tín dụng', 25, 94,  null);
    INSERT_PAYMENT('28/03/2022', 'Tiền mặt', 24, 95,  95);
    INSERT_PAYMENT('29/03/2022', 'Tiền mặt', 26, 96,  null);
    INSERT_PAYMENT('30/03/2022', 'Tiền mặt', 24, 97,  null);
    INSERT_PAYMENT('31/03/2022', 'Thẻ tín dụng', 24, 98,  null);
    INSERT_PAYMENT('14/03/2022', 'Thẻ tín dụng', 27, 99,  null);
    INSERT_PAYMENT('15/03/2022', 'Tiền mặt', 24, 100,  null);

END;

--Procedure INSERT MEMBERSHIP
CREATE OR REPLACE PROCEDURE Insert_MEMBERSHIP( 
                                v_level IN membership.mem_level%TYPE, 
                                v_fee IN membership.mem_fee%type,
                                v_bonus IN membership.mem_bonus%TYPE)
IS
BEGIN
    INSERT INTO MEMBERSHIP (mem_id, mem_level, mem_fee, mem_bonus) 
    VALUES (MEMBERSHIP_SEQ.nextval, v_level, v_fee, v_bonus);
    COMMIT;
END;

SELECT * FROM MEMBERSHIP;
BEGIN
    Insert_MEMBERSHIP('Đồng', 200000, '2 giờ tập mỗi ngày, được ưu tiên xếp lớp');
    Insert_MEMBERSHIP('Bạc', 500000, '4 giờ tập mỗi ngày, được sử dụng phòng tắm miễn phí, ưu tiên xếp lớp');
    Insert_MEMBERSHIP('Vàng', 2000000, 'Thời gian tập luyện không giới hạn, 
    được sử dụng phòng tắm, tủ cá nhân miễn phí, có huấn luyện viên hướng dẫn tập riêng, ưu tiên xếp lớp');
END;

--Procedure INSERT PAYMENT_DETAIL
CREATE OR REPLACE PROCEDURE Insert_PAYMENT_DETAIL(v_payment IN payment_detail.payment_id%TYPE, 
                                v_product IN payment_detail.product_id%TYPE,
                                v_amount IN payment_detail.amount%TYPE)
IS
BEGIN
    INSERT INTO PAYMENT_DETAIL (payment_id, product_id, amount) 
    VALUES (v_payment, v_product, v_amount);
    COMMIT;
END;

SELECT * FROM PAYMENT_DETAIL;
BEGIN
    Insert_PAYMENT_DETAIL(1, 10, 1);
    Insert_PAYMENT_DETAIL(2, 8, 2);
    Insert_PAYMENT_DETAIL(2, 20, 2);
    Insert_PAYMENT_DETAIL(2, 22, 3);
    Insert_PAYMENT_DETAIL(2, 25, 1);
    Insert_PAYMENT_DETAIL(3, 13, 2);
    Insert_PAYMENT_DETAIL(4, 11, 2);
    Insert_PAYMENT_DETAIL(5, 2, 3);
    Insert_PAYMENT_DETAIL(5, 18, 1);
    Insert_PAYMENT_DETAIL(5, 23, 3);
    Insert_PAYMENT_DETAIL(6, 5, 3);
    Insert_PAYMENT_DETAIL(7, 8, 3);
    Insert_PAYMENT_DETAIL(8, 8, 1);
    Insert_PAYMENT_DETAIL(8, 26, 2);
    Insert_PAYMENT_DETAIL(9, 3, 1);
    Insert_PAYMENT_DETAIL(9, 17, 3);
    Insert_PAYMENT_DETAIL(9, 20, 3);
    Insert_PAYMENT_DETAIL(10, 1, 2);
    Insert_PAYMENT_DETAIL(10, 20, 3);
    Insert_PAYMENT_DETAIL(10, 24, 1);
    Insert_PAYMENT_DETAIL(10, 18, 3);
    Insert_PAYMENT_DETAIL(10, 15, 1);
    Insert_PAYMENT_DETAIL(11, 6, 2);
    Insert_PAYMENT_DETAIL(11, 23, 2);
    Insert_PAYMENT_DETAIL(12, 11, 3);
    Insert_PAYMENT_DETAIL(12, 24, 1);
    Insert_PAYMENT_DETAIL(13, 6, 1);
    Insert_PAYMENT_DETAIL(14, 10, 2);
    Insert_PAYMENT_DETAIL(15, 8, 3);
    Insert_PAYMENT_DETAIL(15, 24, 3);
    Insert_PAYMENT_DETAIL(15, 16, 2);
    Insert_PAYMENT_DETAIL(15, 21, 1);
    Insert_PAYMENT_DETAIL(15, 13, 1);
    Insert_PAYMENT_DETAIL(16, 3, 3);
    Insert_PAYMENT_DETAIL(16, 20, 2);
    Insert_PAYMENT_DETAIL(16, 22, 1);
    Insert_PAYMENT_DETAIL(17, 8, 2);
    Insert_PAYMENT_DETAIL(18, 3, 2);
    Insert_PAYMENT_DETAIL(19, 10, 3);
    Insert_PAYMENT_DETAIL(19, 26, 3);
    Insert_PAYMENT_DETAIL(19, 25, 3);
    Insert_PAYMENT_DETAIL(19, 5, 1);
    Insert_PAYMENT_DETAIL(20, 11, 3);
    Insert_PAYMENT_DETAIL(20, 19, 2);
    Insert_PAYMENT_DETAIL(20, 21, 1);
    Insert_PAYMENT_DETAIL(21, 13, 2);
    Insert_PAYMENT_DETAIL(22, 1, 2);
    Insert_PAYMENT_DETAIL(23, 4, 3);
    Insert_PAYMENT_DETAIL(23, 15, 1);
    Insert_PAYMENT_DETAIL(24, 7, 3);
    Insert_PAYMENT_DETAIL(24, 22, 2);
    Insert_PAYMENT_DETAIL(24, 25, 1);
    Insert_PAYMENT_DETAIL(25, 3, 1);
    Insert_PAYMENT_DETAIL(26, 1, 1);
    Insert_PAYMENT_DETAIL(27, 3, 2);
    Insert_PAYMENT_DETAIL(27, 14, 1);
    Insert_PAYMENT_DETAIL(28, 1, 3);
    Insert_PAYMENT_DETAIL(29, 3, 2);
    Insert_PAYMENT_DETAIL(29, 19, 1);
    Insert_PAYMENT_DETAIL(29, 15, 2);
    Insert_PAYMENT_DETAIL(30, 2, 3);
    Insert_PAYMENT_DETAIL(30, 23, 2);
    Insert_PAYMENT_DETAIL(30, 25, 1);
    Insert_PAYMENT_DETAIL(31, 4, 2);
    Insert_PAYMENT_DETAIL(31, 18, 3);
    Insert_PAYMENT_DETAIL(31, 14, 3);
    Insert_PAYMENT_DETAIL(31, 19, 1);
    Insert_PAYMENT_DETAIL(31, 20, 3);
    Insert_PAYMENT_DETAIL(32, 4, 1);
    Insert_PAYMENT_DETAIL(33, 13, 3);
    Insert_PAYMENT_DETAIL(34, 8, 3);
    Insert_PAYMENT_DETAIL(35, 12, 3);
    Insert_PAYMENT_DETAIL(35, 20, 1);
    Insert_PAYMENT_DETAIL(36, 10, 3);
    Insert_PAYMENT_DETAIL(37, 6, 2);
    Insert_PAYMENT_DETAIL(38, 5, 1);
    Insert_PAYMENT_DETAIL(39, 4, 3);
    Insert_PAYMENT_DETAIL(40, 8, 2);
    Insert_PAYMENT_DETAIL(41, 7, 1);
    Insert_PAYMENT_DETAIL(41, 26, 3);
    Insert_PAYMENT_DETAIL(42, 7, 1);
    Insert_PAYMENT_DETAIL(43, 10, 2);
    Insert_PAYMENT_DETAIL(43, 14, 3);
    Insert_PAYMENT_DETAIL(44, 10, 1);
    Insert_PAYMENT_DETAIL(44, 15, 2);
    Insert_PAYMENT_DETAIL(45, 5, 2);
    Insert_PAYMENT_DETAIL(46, 10, 1);
    Insert_PAYMENT_DETAIL(46, 17, 3);
    Insert_PAYMENT_DETAIL(46, 14, 1);
    Insert_PAYMENT_DETAIL(47, 6, 3);
    Insert_PAYMENT_DETAIL(47, 17, 2);
    Insert_PAYMENT_DETAIL(48, 3, 3);
    Insert_PAYMENT_DETAIL(48, 26, 3);
    Insert_PAYMENT_DETAIL(48, 14, 1);
    Insert_PAYMENT_DETAIL(48, 21, 1);
    Insert_PAYMENT_DETAIL(49, 8, 1);
    Insert_PAYMENT_DETAIL(49, 19, 2);
    Insert_PAYMENT_DETAIL(50, 2, 3);
    Insert_PAYMENT_DETAIL(50, 21, 2);
    Insert_PAYMENT_DETAIL(51, 3, 3);
    Insert_PAYMENT_DETAIL(51, 25, 3);
    Insert_PAYMENT_DETAIL(52, 6, 1);
    Insert_PAYMENT_DETAIL(52, 19, 3);
    Insert_PAYMENT_DETAIL(53, 3, 2);
    Insert_PAYMENT_DETAIL(54, 8, 3);
    Insert_PAYMENT_DETAIL(54, 17, 2);
    Insert_PAYMENT_DETAIL(54, 23, 3);
    Insert_PAYMENT_DETAIL(55, 4, 1);
    Insert_PAYMENT_DETAIL(55, 17, 3);
    Insert_PAYMENT_DETAIL(55, 26, 3);
    Insert_PAYMENT_DETAIL(56, 11, 3);
    Insert_PAYMENT_DETAIL(56, 19, 3);
    Insert_PAYMENT_DETAIL(57, 5, 1);
    Insert_PAYMENT_DETAIL(58, 1, 3);
    Insert_PAYMENT_DETAIL(59, 3, 2);
    Insert_PAYMENT_DETAIL(59, 17, 3);
    Insert_PAYMENT_DETAIL(59, 16, 3);
    Insert_PAYMENT_DETAIL(60, 13, 1);
    Insert_PAYMENT_DETAIL(60, 17, 1);
    Insert_PAYMENT_DETAIL(60, 26, 1);
    Insert_PAYMENT_DETAIL(61, 7, 1);
    Insert_PAYMENT_DETAIL(61, 24, 2);
    Insert_PAYMENT_DETAIL(62, 13, 3);
    Insert_PAYMENT_DETAIL(62, 23, 3);
    Insert_PAYMENT_DETAIL(63, 8, 2);
    Insert_PAYMENT_DETAIL(64, 13, 2);
    Insert_PAYMENT_DETAIL(64, 25, 3);
    Insert_PAYMENT_DETAIL(65, 10, 1);
    Insert_PAYMENT_DETAIL(65, 11, 3);
    Insert_PAYMENT_DETAIL(65, 22, 1);
    Insert_PAYMENT_DETAIL(66, 13, 1);
    Insert_PAYMENT_DETAIL(67, 4, 3);
    Insert_PAYMENT_DETAIL(67, 23, 3);
    Insert_PAYMENT_DETAIL(68, 7, 2);
    Insert_PAYMENT_DETAIL(68, 21, 2);
    Insert_PAYMENT_DETAIL(68, 15, 1);
    Insert_PAYMENT_DETAIL(68, 16, 2);
    Insert_PAYMENT_DETAIL(68, 17, 1);
    Insert_PAYMENT_DETAIL(69, 11, 3);
    Insert_PAYMENT_DETAIL(70, 9, 3);
    Insert_PAYMENT_DETAIL(70, 23, 2);
    Insert_PAYMENT_DETAIL(70, 24, 3);
    Insert_PAYMENT_DETAIL(71, 7, 2);
    Insert_PAYMENT_DETAIL(72, 8, 3);
    Insert_PAYMENT_DETAIL(72, 19, 2);
    Insert_PAYMENT_DETAIL(72, 21, 2);
    Insert_PAYMENT_DETAIL(72, 22, 3);
    Insert_PAYMENT_DETAIL(73, 12, 2);
    Insert_PAYMENT_DETAIL(74, 6, 1);
    Insert_PAYMENT_DETAIL(74, 22, 1);
    Insert_PAYMENT_DETAIL(75, 3, 3);
    Insert_PAYMENT_DETAIL(75, 19, 3);
    Insert_PAYMENT_DETAIL(75, 26, 2);
    Insert_PAYMENT_DETAIL(76, 7, 1);
    Insert_PAYMENT_DETAIL(77, 9, 1);
    Insert_PAYMENT_DETAIL(77, 16, 1);
    Insert_PAYMENT_DETAIL(78, 11, 1);
    Insert_PAYMENT_DETAIL(79, 13, 1);
    Insert_PAYMENT_DETAIL(79, 16, 1);
    Insert_PAYMENT_DETAIL(80, 11, 3);
    Insert_PAYMENT_DETAIL(80, 14, 1);
    Insert_PAYMENT_DETAIL(81, 1, 1);
    Insert_PAYMENT_DETAIL(81, 18, 2);
    Insert_PAYMENT_DETAIL(82, 4, 3);
    Insert_PAYMENT_DETAIL(83, 12, 2);
    Insert_PAYMENT_DETAIL(83, 20, 2);
    Insert_PAYMENT_DETAIL(84, 6, 3);
    Insert_PAYMENT_DETAIL(84, 14, 1);
    Insert_PAYMENT_DETAIL(84, 24, 3);
    Insert_PAYMENT_DETAIL(85, 12, 3);
    Insert_PAYMENT_DETAIL(85, 25, 2);
    Insert_PAYMENT_DETAIL(86, 3, 2);
    Insert_PAYMENT_DETAIL(87, 3, 3);
    Insert_PAYMENT_DETAIL(87, 17, 1);
    Insert_PAYMENT_DETAIL(88, 13, 2);
    Insert_PAYMENT_DETAIL(88, 24, 3);
    Insert_PAYMENT_DETAIL(88, 21, 2);
    Insert_PAYMENT_DETAIL(88, 15, 2);
    Insert_PAYMENT_DETAIL(89, 8, 1);
    Insert_PAYMENT_DETAIL(90, 6, 2);
    Insert_PAYMENT_DETAIL(91, 9, 1);
    Insert_PAYMENT_DETAIL(91, 22, 1);
    Insert_PAYMENT_DETAIL(92, 11, 3);
    Insert_PAYMENT_DETAIL(92, 17, 1);
    Insert_PAYMENT_DETAIL(93, 10, 1);
    Insert_PAYMENT_DETAIL(93, 21, 2);
    Insert_PAYMENT_DETAIL(93, 14, 2);
    Insert_PAYMENT_DETAIL(94, 8, 2);
    Insert_PAYMENT_DETAIL(95, 9, 1);
    Insert_PAYMENT_DETAIL(96, 4, 3);
    Insert_PAYMENT_DETAIL(96, 17, 2);
    Insert_PAYMENT_DETAIL(96, 26, 1);
    Insert_PAYMENT_DETAIL(96, 21, 2);
    Insert_PAYMENT_DETAIL(97, 3, 2);
    Insert_PAYMENT_DETAIL(97, 23, 3);
    Insert_PAYMENT_DETAIL(97, 18, 1);
    Insert_PAYMENT_DETAIL(98, 13, 3);
    Insert_PAYMENT_DETAIL(99, 4, 1);
    Insert_PAYMENT_DETAIL(100, 19, 1);
    Insert_PAYMENT_DETAIL(100, 20, 3);

END;

--Procedure INSERT SIGNUP_CLASS
CREATE OR REPLACE PROCEDURE Insert_SIGNUP_CLASS(v_payment IN signup_class.payment_id%TYPE, 
                                v_customer IN signup_class.cus_id%TYPE, 
                                v_class IN signup_class.class_id%type,
                                v_date IN signup_class.signup_date%TYPE)
IS
BEGIN
    INSERT INTO SIGNUP_CLASS (payment_id, cus_id, class_id, signup_date) 
    VALUES (v_payment, v_customer, v_class, v_date);
    COMMIT;
END;

SELECT * FROM SIGNUP_CLASS;
BEGIN
    INSERT_SIGNUP_CLASS(20, 20, 16, '21/03/2022');
    INSERT_SIGNUP_CLASS(21, 21, 21, '22/03/2022');
    INSERT_SIGNUP_CLASS(22, 22, 7, '23/03/2022');
    INSERT_SIGNUP_CLASS(23, 23, 15, '24/03/2022');
    INSERT_SIGNUP_CLASS(24, 24, 19, '25/03/2022');
    INSERT_SIGNUP_CLASS(25, 25, 9, '28/03/2022');
    INSERT_SIGNUP_CLASS(26, 26, 22, '29/03/2022');
    INSERT_SIGNUP_CLASS(27, 27, 7, '30/03/2022');
    INSERT_SIGNUP_CLASS(28, 28, 8, '31/03/2022');
    INSERT_SIGNUP_CLASS(29, 29, 9, '14/03/2022');
    INSERT_SIGNUP_CLASS(30, 30, 22, '15/03/2022');
    INSERT_SIGNUP_CLASS(31, 31, 22, '16/03/2022');
    INSERT_SIGNUP_CLASS(32, 32, 17, '17/03/2022');
    INSERT_SIGNUP_CLASS(33, 33, 4, '18/03/2022');
    INSERT_SIGNUP_CLASS(34, 34, 8, '21/03/2022');
    INSERT_SIGNUP_CLASS(35, 35, 3, '22/03/2022');
    INSERT_SIGNUP_CLASS(36, 36, 19, '23/03/2022');
    INSERT_SIGNUP_CLASS(37, 37, 4, '24/03/2022');
    INSERT_SIGNUP_CLASS(38, 38, 18, '25/03/2022');
    INSERT_SIGNUP_CLASS(39, 39, 17, '28/03/2022');
    INSERT_SIGNUP_CLASS(40, 40, 17, '29/03/2022');
    INSERT_SIGNUP_CLASS(41, 41, 9, '30/03/2022');
    INSERT_SIGNUP_CLASS(42, 42, 12, '31/03/2022');
    INSERT_SIGNUP_CLASS(43, 43, 20, '14/03/2022');
    INSERT_SIGNUP_CLASS(44, 44, 7, '15/03/2022');
    INSERT_SIGNUP_CLASS(45, 45, 21, '16/03/2022');
    INSERT_SIGNUP_CLASS(46, 46, 17, '17/03/2022');
    INSERT_SIGNUP_CLASS(47, 47, 11, '18/03/2022');
    INSERT_SIGNUP_CLASS(48, 48, 19, '21/03/2022');
    INSERT_SIGNUP_CLASS(49, 49, 5, '22/03/2022');
    INSERT_SIGNUP_CLASS(50, 50, 19, '23/03/2022');

END;



--Procedure INSERT SIGNUP_MEMBERSHIP
CREATE OR REPLACE PROCEDURE Insert_SIGNUP_MEMBERSHIP(v_payment IN signup_membership.payment_id%TYPE, 
                                v_customer IN signup_membership.cus_id%TYPE, 
                                v_mem IN signup_membership.mem_id%type,
                                v_date IN signup_membership.signup_date%TYPE)
IS
BEGIN
    INSERT INTO SIGNUP_membership (payment_id, cus_id, mem_id, signup_date) 
    VALUES (v_payment, v_customer, v_mem, v_date);
    COMMIT;
END;

SELECT * FROM SIGNUP_MEMBERSHIP;
BEGIN
    Insert_SIGNUP_MEMBERSHIP(1, 1, 3, '14/03/2022');
    Insert_SIGNUP_MEMBERSHIP(2, 2, 2, '15/03/2022');
    Insert_SIGNUP_MEMBERSHIP(3, 3, 1, '16/03/2022');
    Insert_SIGNUP_MEMBERSHIP(4, 4, 3, '17/03/2022');
    Insert_SIGNUP_MEMBERSHIP(5, 5, 2, '18/03/2022');
    Insert_SIGNUP_MEMBERSHIP(6, 6, 3, '21/03/2022');
    Insert_SIGNUP_MEMBERSHIP(7, 7, 3, '22/03/2022');
    Insert_SIGNUP_MEMBERSHIP(8, 8, 2, '23/03/2022');
    Insert_SIGNUP_MEMBERSHIP(9, 9, 1, '24/03/2022');
    Insert_SIGNUP_MEMBERSHIP(10, 10, 2, '25/03/2022');
    Insert_SIGNUP_MEMBERSHIP(11, 11, 1, '28/03/2022');
    Insert_SIGNUP_MEMBERSHIP(12, 12, 1, '29/03/2022');
    Insert_SIGNUP_MEMBERSHIP(13, 13, 2, '30/03/2022');
    Insert_SIGNUP_MEMBERSHIP(14, 14, 2, '31/03/2022');
    Insert_SIGNUP_MEMBERSHIP(15, 15, 3, '14/03/2022');
    Insert_SIGNUP_MEMBERSHIP(16, 16, 2, '15/03/2022');
    Insert_SIGNUP_MEMBERSHIP(17, 17, 2, '16/03/2022');
    Insert_SIGNUP_MEMBERSHIP(18, 18, 1, '17/03/2022');
    Insert_SIGNUP_MEMBERSHIP(19, 19, 3, '18/03/2022');
    Insert_SIGNUP_MEMBERSHIP(20, 20, 3, '21/03/2022');
    Insert_SIGNUP_MEMBERSHIP(21, 21, 3, '22/03/2022');
    Insert_SIGNUP_MEMBERSHIP(22, 22, 1, '23/03/2022');
    Insert_SIGNUP_MEMBERSHIP(23, 23, 1, '24/03/2022');
    Insert_SIGNUP_MEMBERSHIP(24, 24, 1, '25/03/2022');
    Insert_SIGNUP_MEMBERSHIP(25, 25, 1, '28/03/2022');
    Insert_SIGNUP_MEMBERSHIP(26, 26, 3, '29/03/2022');
    Insert_SIGNUP_MEMBERSHIP(27, 27, 1, '30/03/2022');
    Insert_SIGNUP_MEMBERSHIP(28, 28, 3, '31/03/2022');
    Insert_SIGNUP_MEMBERSHIP(29, 29, 1, '14/03/2022');
    Insert_SIGNUP_MEMBERSHIP(30, 30, 1, '15/03/2022');
END;

--Procedure INSERT INBODY
CREATE OR REPLACE PROCEDURE Insert_INBODY(v_cus IN inbody.cus_id%TYPE,
                                            v_muscle IN inbody.muscle_mass%TYPE, v_body_fat IN inbody.body_fat_mass%TYPE,
                                            v_weight IN inbody.weight%TYPE, v_height IN inbody.height%TYPE,
                                            v_waist IN inbody.waist%TYPE, v_hip IN inbody.hip%TYPE,
                                            v_protein IN inbody.protein%TYPE, v_mineral IN inbody.mineral%TYPE,
                                            v_TBW IN inbody.tbw%TYPE)
IS
    v_bmi   inbody.bmi%TYPE;
    v_pbf   inbody.pbf%TYPE;
    v_whr   inbody.whr%TYPE;
    v_score inbody.inbody_score%TYPE;
BEGIN
    v_bmi:= ROUND((v_weight / (v_height * 2)), 2);
    v_pbf:= ROUND((v_body_fat / v_weight * 100), 2);
    v_whr:= ROUND((v_waist / v_hip), 3);
    v_score := ROUND(100 - v_pbf);
    INSERT INTO INBODY (cus_id, inbody_date, muscle_mass, body_fat_mass,
                                weight, height, waist, hip, protein, mineral, tbw, bmi, pbf, whr, inbody_score)
    VALUES (v_cus, sysdate, v_muscle, v_body_fat, v_weight, v_height, v_waist, v_hip,
            v_protein, v_mineral, v_TBW, v_bmi, v_pbf, v_whr, v_score);
    COMMIT;
END;

SELECT * FROM INBODY;

BEGIN
    Insert_INBODY(1, 31.49, 12.3, 69.97, 1.72, 110, 94.5, 84.38, 4.21, 48.98);
    Insert_INBODY(2, 24.37, 6.1, 78.59, 1.84, 112, 98.7, 110.52, 4.06, 55.01);
    Insert_INBODY(3, 21.66, 25.3, 69.86, 1.68, 61, 99.2, 73.03, 4.57, 48.9);
    Insert_INBODY(4, 45.26, 10.4, 83.81, 1.84, 108, 101.2, 125.69, 5.28, 58.67);
    Insert_INBODY(5, 43.46, 28.7, 83.58, 1.81, 80, 101.9, 108.94, 5.45, 58.51);
    Insert_INBODY(6, 34.34, 20.9, 95.37, 1.9, 81, 107.8, 106.6, 6.17, 66.76);
    Insert_INBODY(7, 33.67, 19.2, 82.11, 1.77, 84, 100.3, 107.59, 5.73, 57.48);
    Insert_INBODY(8, 38.32, 12.4, 79.84, 1.84, 92, 97.1, 102.17, 4.52, 55.89);
    Insert_INBODY(9, 45.92, 4.1, 86.64, 1.88, 78, 99.9, 104.64, 4.51, 60.65);
    Insert_INBODY(10, 46.77, 11.7, 89.93, 1.87, 73, 104.1, 103.12, 6.01, 62.95);
    Insert_INBODY(11, 40.56, 7.1, 84.49, 1.89, 64, 98.2, 117.82, 5.54, 59.14);
    Insert_INBODY(12, 36.26, 7.8, 97.98, 1.93, 88, 107.7, 99.71, 5.13, 68.59);
    Insert_INBODY(13, 36.03, 20.8, 81.88, 1.77, 65, 103.9, 106.2, 5.04, 57.32);
    Insert_INBODY(14, 37.25, 21.2, 93.11, 1.81, 55, 108.6, 118.02, 5.43, 65.18);
    Insert_INBODY(15, 41.73, 22.1, 85.17, 1.77, 109, 100.1, 92.02, 4.68, 59.62);
    Insert_INBODY(16, 35.44, 20.9, 73.83, 1.68, 117, 99.2, 100.66, 3.89, 51.68);
    Insert_INBODY(17, 29.31, 29, 88.8, 1.8, 56, 105.2, 130.05, 5.16, 62.16);
    Insert_INBODY(18, 35.12, 22.9, 94.92, 1.8, 106, 107, 115.09, 6.15, 66.45);
    Insert_INBODY(19, 27.51, 16, 83.35, 1.72, 78, 102.4, 123.77, 5.29, 58.35);
    Insert_INBODY(20, 31.7, 16.5, 96.05, 1.87, 89, 109, 130.68, 5.3, 67.24);
    Insert_INBODY(21, 26.8, 19.1, 81.2, 1.73, 120, 104.9, 118.06, 5.27, 56.84);
    Insert_INBODY(22, 35.47, 15.2, 90.95, 1.77, 106, 104.8, 123.48, 5.49, 63.67);
    Insert_INBODY(23, 24.18, 15.6, 63.62, 1.73, 67, 94.6, 66.56, 3.2, 44.54);
    Insert_INBODY(24, 29.69, 17.7, 67.48, 1.78, 56, 93.4, 83.13, 4.22, 47.24);
    Insert_INBODY(25, 28.13, 14, 68.61, 1.72, 96, 95.8, 72.75, 4.07, 48.03);
    Insert_INBODY(26, 35.4, 3.7, 72.24, 1.82, 118, 96.5, 78.92, 4.01, 50.57);
    Insert_INBODY(27, 31.62, 7.9, 59.65, 1.71, 118, 85.3, 81.84, 3.49, 41.76);
    Insert_INBODY(28, 25.52, 22.9, 67.14, 1.71, 103, 94.7, 74.74, 4.48, 47);
    Insert_INBODY(29, 21.76, 3.7, 60.45, 1.64, 108, 88.5, 79.17, 3.67, 42.31);
    Insert_INBODY(30, 35, 8.8, 72.92, 1.75, 60, 98.7, 74.51, 4.22, 51.05);
    Insert_INBODY(31, 44.58, 11.9, 82.56, 1.87, 120, 99.8, 122.52, 4.49, 57.79);
    Insert_INBODY(32, 26.17, 5.7, 72.69, 1.81, 75, 100.6, 89.23, 4.73, 50.89);
    Insert_INBODY(33, 36.58, 11.8, 76.21, 1.81, 91, 94.5, 90.39, 3.92, 53.35);
    Insert_INBODY(34, 49.56, 21.3, 99.12, 1.8, 83, 108.3, 109.54, 6.65, 69.38);
    Insert_INBODY(35, 41.5, 32.3, 112.16, 1.87, 119, 116.1, 147.44, 6.53, 78.51);
    Insert_INBODY(36, 45.23, 40.1, 86.98, 1.65, 77, 113.8, 114.02, 5.09, 60.89);
    Insert_INBODY(37, 37.62, 24.2, 91.74, 1.78, 120, 106.2, 95.03, 5.95, 64.22);
    Insert_INBODY(38, 43.74, 28.4, 89.25, 1.73, 86, 104.8, 102.69, 4.78, 62.48);
    Insert_INBODY(39, 59.31, 35.2, 164.73, 1.84, 111, 147.7, 189.17, 9.01, 115.31);
    Insert_INBODY(40, 33.15, 32.6, 92.09, 1.7, 110, 102.5, 92.96, 5.31, 64.46);
    Insert_INBODY(41, 33.38, 34.5, 119.19, 1.75, 58, 125.6, 146.04, 6.59, 83.43);
    Insert_INBODY(42, 31.62, 32.9, 92.99, 0.75, 74, 115.5, 99.31, 5.15, 65.1);
    Insert_INBODY(43, 36.42, 31.6, 98.44, 1.78, 66, 114.1, 115.38, 5.77, 68.91);
    Insert_INBODY(44, 32.7, 32, 96.17, 1.82, 86, 106, 137.25, 5.45, 67.32);
    Insert_INBODY(45, 29.55, 7.7, 56.82, 1.73, 95, 88.2, 56.99, 3.97, 39.77);
    Insert_INBODY(46, 36.51, 13.9, 74.51, 1.86, 106, 97.2, 76.32, 4.9, 52.16);
    Insert_INBODY(47, 18.17, 10.8, 60.56, 1.71, 113, 88.5, 69.87, 3.9, 42.39);
    Insert_INBODY(48, 31.66, 5.6, 67.36, 1.81, 80, 92.7, 70.97, 3.71, 47.16);
    Insert_INBODY(49, 32.64, 13.6, 61.58, 1.74, 90, 90.4, 79.12, 4.06, 43.11);
    Insert_INBODY(50, 17.36, 4, 57.84, 1.7, 118, 87.2, 61.08, 3.68, 40.49);
    Insert_INBODY(51, 22.98, 10.2, 71.79, 1.84, 90, 98.3, 71.92, 4.35, 50.25);
    Insert_INBODY(52, 31.59, 6.6, 63.17, 1.75, 67, 91, 90.49, 4.23, 44.22);
    Insert_INBODY(53, 17.44, 8, 62.26, 1.72, 70, 89.1, 75.15, 3.38, 43.58);
    Insert_INBODY(54, 22.87, 6.3, 69.29, 1.87, 83, 91.6, 91.92, 4.43, 48.51);
    Insert_INBODY(55, 29.05, 3.9, 61.81, 1.71, 70, 88.6, 65.63, 3.5, 43.27);
    Insert_INBODY(56, 42.22, 22.6, 89.82, 1.83, 79, 99.6, 124.07, 5.27, 62.87);
    Insert_INBODY(57, 23.06, 20.4, 82.33, 1.73, 74, 102.5, 120.24, 5.53, 57.63);
    Insert_INBODY(58, 31.96, 28, 91.29, 1.77, 66, 105.8, 98.34, 5.2, 63.91);
    Insert_INBODY(59, 43.18, 31.5, 91.86, 1.8, 89, 97, 111.61, 5.78, 64.3);
    Insert_INBODY(60, 31.8, 24.6, 81.54, 1.67, 69, 99.6, 82.92, 5.14, 57.08);
    Insert_INBODY(61, 42.14, 26.1, 97.98, 1.86, 75, 103.1, 101.83, 6.15, 68.59);
    Insert_INBODY(62, 22.71, 29.8, 81.09, 1.74, 77, 100.8, 89.7, 5.43, 56.76);
    Insert_INBODY(63, 27.18, 30.7, 87.66, 1.78, 81, 99.4, 101.64, 5.58, 61.37);
    Insert_INBODY(64, 43.6, 25.8, 80.75, 1.7, 94, 99.7, 101.05, 4.97, 56.52);
    Insert_INBODY(65, 36.36, 32.3, 93.22, 1.78, 93, 108.3, 96.88, 5.62, 65.25);
    Insert_INBODY(66, 35.8, 30, 83.24, 1.71, 79, 104.2, 91.9, 4.26, 58.27);
    Insert_INBODY(67, 28.18, 21.5, 68.72, 1.8, 106, 93.9, 73.42, 4.71, 48.11);
    Insert_INBODY(68, 33.7, 13.8, 70.2, 1.82, 106, 91.8, 96.71, 4.2, 49.14);
    Insert_INBODY(69, 19.72, 6.3, 70.43, 1.76, 116, 96.1, 81.63, 4.35, 49.3);
    Insert_INBODY(70, 29.16, 12.9, 71.11, 1.82, 91, 94.3, 103.79, 4.19, 49.78);
    Insert_INBODY(71, 32.68, 24.3, 75.98, 1.82, 96, 98.5, 83.42, 5.21, 53.19);
    Insert_INBODY(72, 35.95, 8.8, 66.57, 1.75, 99, 95.5, 93.69, 3.69, 46.6);
    Insert_INBODY(73, 36.46, 8.5, 72.92, 1.87, 74, 96.3, 99, 5, 51.05);
    Insert_INBODY(74, 23.82, 13.5, 56.7, 1.63, 83, 88.6, 81.15, 3.33, 39.69);
    Insert_INBODY(75, 25.95, 11.8, 64.87, 1.67, 76, 93, 85.69, 4.02, 45.41);
    Insert_INBODY(76, 25.56, 18.5, 67.25, 1.71, 64, 94.8, 93.18, 3.72, 47.08);
    Insert_INBODY(77, 23.59, 8.8, 73.71, 1.77, 115, 94.3, 83.71, 4.86, 51.6);
    Insert_INBODY(78, 43.54, 22.2, 80.63, 1.74, 71, 98.3, 81.9, 4.89, 56.44);
    Insert_INBODY(79, 38.77, 21.5, 73.15, 1.78, 76, 99.3, 103.6, 3.77, 51.2);
    Insert_INBODY(80, 30.3, 18.8, 77.68, 1.76, 95, 100.2, 116.33, 4.75, 54.38);
    Insert_INBODY(81, 40.11, 31.4, 74.28, 1.72, 66, 97.1, 111.12, 4.4, 52);
    Insert_INBODY(82, 29.31, 26.8, 68.16, 1.71, 88, 96.9, 68.9, 4.61, 47.71);
    Insert_INBODY(83, 45.74, 18.4, 86.3, 1.85, 79, 99.6, 110.98, 5.79, 60.41);
    Insert_INBODY(84, 28.66, 27, 77.46, 1.78, 90, 95, 80.15, 5.03, 54.22);
    Insert_INBODY(85, 28.2, 27, 76.21, 1.76, 88, 96.2, 109.4, 4.09, 53.35);
    Insert_INBODY(86, 28.79, 26.6, 75.76, 1.71, 105, 96.2, 100.2, 4.75, 53.03);
    Insert_INBODY(87, 27.2, 14.9, 71.56, 1.71, 102, 96.9, 95.53, 4.3, 50.09);
    Insert_INBODY(88, 37.02, 23.1, 72.58, 1.67, 77, 93.8, 72.73, 4.33, 50.81);
    Insert_INBODY(89, 23.26, 8.3, 80.18, 1.84, 80, 99.3, 99.57, 5.19, 56.13);
    Insert_INBODY(90, 31.14, 14.1, 79.84, 1.85, 68, 98.3, 82.01, 5.07, 55.89);
    Insert_INBODY(91, 25.7, 20.5, 80.29, 1.78, 115, 102.2, 112.56, 4.55, 56.21);
    Insert_INBODY(92, 22.83, 18.2, 81.54, 1.77, 79, 100.6, 98.88, 5.7, 57.08);
    Insert_INBODY(93, 38.23, 8.5, 74.96, 1.79, 94, 95.4, 82.36, 5.09, 52.47);
    Insert_INBODY(94, 27.07, 24.9, 87.32, 1.82, 69, 100.6, 98.09, 4.58, 61.13);
    Insert_INBODY(95, 25.08, 9, 83.58, 1.89, 63, 101.4, 94.99, 5.34, 58.51);
    Insert_INBODY(96, 49.9, 17.4, 101.84, 1.97, 74, 107.5, 132.99, 5.95, 71.29);
    Insert_INBODY(97, 39.39, 9.6, 85.62, 1.86, 93, 102.4, 102.31, 4.62, 59.94);
    Insert_INBODY(98, 39.81, 11.3, 73.71, 1.69, 114, 96.2, 85.27, 3.85, 51.6);
    Insert_INBODY(99, 34.08, 17.8, 70.99, 1.73, 55, 92.8, 72, 4.83, 49.7);
    Insert_INBODY(100, 25.95, 11.8, 64.87, 1.67, 76, 93, 85.69, 4.02, 45.41);

END;


--------------------------------------------
---------- PROCEDURE IN THONG TIN ----------
--------------------------------------------
-- Store thong tin khach hang theo ma khach hang
CREATE OR REPLACE PROCEDURE pro_print_customer (print_id IN customer.cus_id%type)
IS
    print_name        customer.cus_name%type;
    print_birthday    customer.cus_birthday%type;
    print_datejoin    customer.cus_datejoin%type;
    print_gender      customer.cus_gender%type;
    print_expired     customer.cus_memexpired%type;
    print_address     customer.cus_address%type;
    print_telephone   customer.cus_telephone%type;
    print_revenue     customer.cus_revenue%type;
BEGIN
    SELECT cus_name, cus_birthday, cus_datejoin, cus_gender, cus_memexpired,
            cus_address, cus_telephone, cus_revenue INTO print_name, print_birthday,
            print_datejoin, print_gender, print_expired, print_address,
            print_telephone, print_revenue
    FROM customer
    WHERE cus_id = print_id;
    DBMS_OUTPUT.PUT_LINE('Thông tin của khách hàng: '|| print_id);
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Họ tên: ' || print_name);
    DBMS_OUTPUT.PUT_LINE('Ngày sinh: ' || print_birthday);
    DBMS_OUTPUT.PUT_LINE('Ngày tham gia: '|| print_datejoin);
    DBMS_OUTPUT.PUT_LINE('Giới tính: '|| print_gender);
    DBMS_OUTPUT.PUT_LINE('Ngày hết hạn: ' || print_expired);
    DBMS_OUTPUT.PUT_LINE('Địa chỉ: '|| print_address);
    DBMS_OUTPUT.PUT_LINE('Số điện thoại: '|| print_telephone);
    DBMS_OUTPUT.PUT_LINE('Tích lũy: '|| print_revenue);
    DBMS_OUTPUT.PUT_LINE('-----------------------------------------------------------');
END;

BEGIN
    pro_print_customer('1');
END;

-- Store thong tin khach hang theo so dien thoai
CREATE OR REPLACE PROCEDURE pro_print_telephone (print_telephone IN customer.cus_telephone%type)
IS
    print_id          customer.cus_id%type;
    print_name        customer.cus_name%type;
    print_birthday    customer.cus_birthday%type;
    print_datejoin    customer.cus_datejoin%type;
    print_gender      customer.cus_gender%type;
    print_expired     customer.cus_memexpired%type;
    print_address     customer.cus_address%type;
    print_revenue     customer.cus_revenue%type;
BEGIN
    SELECT cus_id, cus_name, cus_birthday, cus_datejoin, cus_gender, cus_memexpired,
            cus_address, cus_revenue INTO print_id, print_name, print_birthday,
            print_datejoin, print_gender, print_expired, print_address, print_revenue
    FROM customer
    WHERE cus_telephone = print_telephone;
    DBMS_OUTPUT.PUT_LINE('Thông tin của khách hàng: ');
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Mã khách hàng: ' || print_id);
    DBMS_OUTPUT.PUT_LINE('Họ tên: ' || print_name);
    DBMS_OUTPUT.PUT_LINE('Ngày sinh: ' || print_birthday);
    DBMS_OUTPUT.PUT_LINE('Ngày tham gia: '|| print_datejoin);
    DBMS_OUTPUT.PUT_LINE('Giới tính: '|| print_gender);
    DBMS_OUTPUT.PUT_LINE('Ngày hết hạn: ' || print_expired);
    DBMS_OUTPUT.PUT_LINE('Địa chỉ: '|| print_address);
    DBMS_OUTPUT.PUT_LINE('Số điện thoại: '|| print_telephone);
    DBMS_OUTPUT.PUT_LINE('Tích lũy: '|| print_revenue);
    DBMS_OUTPUT.PUT_LINE('-----------------------------------------------------------');
END;

BEGIN
    pro_print_telephone('0342113781');
END;

-- Store thong tin san pham
CREATE OR REPLACE PROCEDURE pro_product(print_id IN product.product_id%TYPE)
IS
    product_name_in     product.product_name%TYPE;
    product_type_in     product.product_type%TYPE;
    product_price_in    product.product_price%TYPE;
    product_amount_in   product.product_amount%TYPE;
BEGIN
    SELECT product_name, product_type, product_price, product_amount INTO 
            product_name_in, product_type_in, product_price_in, product_amount_in
    FROM product
    WHERE product_id = print_id;
    DBMS_OUTPUT.PUT_LINE('Thông tin sản phẩm: ' || print_id);
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('-- Tên sản phẩm: '|| product_name_in ||' -- Loại sản phẩm: '|| product_type_in ||'');
    DBMS_OUTPUT.PUT_LINE('**** Giá bán: '|| product_price_in || 'đ');
    DBMS_OUTPUT.PUT_LINE('Số lượng: ' || product_amount_in);
    DBMS_OUTPUT.PUT_LINE('-----------------------------------------------------------');
END;

BEGIN
    pro_product('1');
END;

-- Store thong tin hoa don --
CREATE OR REPLACE PROCEDURE pro_print_payment(print_id IN payment.payment_id%TYPE)
AS
    customer_in    payment.cus_id%TYPE;
    staff_in       payment.staff_id%TYPE;
    discount_in    payment.dis_id%TYPE;
    lastday_in     payment.payment_lastday%TYPE;
    mode_in        payment.payment_mode%TYPE;
    total_in       payment.total%TYPE;
    
BEGIN
    SELECT cus_id, staff_id, dis_id, payment_lastday, payment_mode, total INTO 
            customer_in, staff_in, discount_in, lastday_in, mode_in, total_in
    FROM PAYMENT
    WHERE payment_id = print_id;
    DBMS_OUTPUT.PUT_LINE('Thông tin hóa đơn: ' || print_id);
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Khách hàng: ' || customer_in);
    DBMS_OUTPUT.PUT_LINE('Nhân viên lập hóa đơn: ' || staff_in);
    DBMS_OUTPUT.PUT_LINE('Mã giảm giá: ' || discount_in);
    DBMS_OUTPUT.PUT_LINE('Ngày thanh toán: ' || lastday_in);
    DBMS_OUTPUT.PUT_LINE('Hình thức thanh toán: ' || mode_in);
    DBMS_OUTPUT.PUT_LINE('--------------------------------');
    DBMS_OUTPUT.PUT_LINE('Số tiền phải trả: ' || total_in);
END;

BEGIN
    pro_print_payment('1');
END;

-- In thong tin INBODY
-- function cap nhat ngay xuat INBODY la ngay hien tai. 
CREATE OR REPLACE FUNCTION update_into_sys RETURN date
IS
    sys_inbody_date     date;
BEGIN
    sys_inbody_date :=sysdate;
    return sys_inbody_date;
END;
-- procedure in thông tin inbody
CREATE OR REPLACE PROCEDURE pro_print_inbody (print_id IN customer.cus_id%type)
IS
    print_date          inbody.inbody_date%type;
    print_muscle        inbody.muscle_mass%type;
    print_bodyfat       inbody.body_fat_mass%type;
    print_weight        inbody.weight%type;
    print_height        inbody.height%type;
    print_waist         inbody.waist%type;
    print_hip           inbody.hip%type;
    print_protein       inbody.protein%type;
    print_mineral       inbody.mineral%type;
    print_TBW           inbody.tbw%TYPE;
    print_BMI           inbody.bmi%TYPE;
    print_PBF           inbody.pbf%TYPE;
    print_WHR           inbody.whr%TYPE;
    print_score         inbody.inbody_score%TYPE;
    print_sys           date;
BEGIN
    SELECT inbody_date, muscle_mass, body_fat_mass, weight, height, waist, hip,
            protein, mineral, tbw, bmi, pbf, whr, inbody_score, update_into_sys INTO print_date,
            print_muscle, print_bodyfat, print_weight, print_height, print_waist,
            print_hip, print_protein, print_mineral, print_tbw, print_bmi, print_pbf,
            print_whr, print_score,  print_sys
    FROM inbody
    WHERE cus_id = print_id;
    
    
    DBMS_OUTPUT.PUT_LINE('Ngày lập: '||print_sys);
    DBMS_OUTPUT.PUT_LINE('Thông tin InBody của khách hàng ' || print_id || ' vào ngày ' || print_date);
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Khối lượng cơ: ' || print_muscle);
    DBMS_OUTPUT.PUT_LINE('Khối lượng mỡ: ' || print_bodyfat);
    DBMS_OUTPUT.PUT_LINE('Cân nặng: '|| print_weight);
    DBMS_OUTPUT.PUT_LINE('Chiều cao: '|| print_height);
    DBMS_OUTPUT.PUT_LINE('Số đo vòng bụng: ' || print_waist);
    DBMS_OUTPUT.PUT_LINE('Số đo vòng hông: ' || print_hip);
    DBMS_OUTPUT.PUT_LINE('Khối lượng protein trong cơ thể: '|| print_protein);
    DBMS_OUTPUT.PUT_LINE('Khối lượng khoáng chất trong cơ thể: '|| print_mineral);
    DBMS_OUTPUT.PUT_LINE('Khối lượng nước trong cơ thể: '|| print_tbw);
    DBMS_OUTPUT.PUT_LINE('Chỉ số BMI: '|| print_bmi);
    DBMS_OUTPUT.PUT_LINE('Chỉ số PBF: '|| print_pbf);
    DBMS_OUTPUT.PUT_LINE('Chỉ số WHR: '|| print_whr);
    DBMS_OUTPUT.PUT_LINE('Điểm cơ thể: '|| print_score);
    DBMS_OUTPUT.PUT_LINE('-----------------------------------------------------------');
END;

------------------------------------------
--------- PROCEDURE TINH TOAN ------------
------------------------------------------
-- Tinh payment_total

--- Function Tong tien san pham
BEGIN sumPaymentTotal(24); END;

CREATE OR REPLACE FUNCTION sumPaymentProduct(PAY_ID PAYMENT.payment_id%TYPE)
RETURN INT  
AS
    print_payment_id        PAYMENT.payment_id%type;
    print_total_sell        INT := 0;
    countS                  INT := 0;
BEGIN
    select count(*) into countS
    from PAYMENT_DETAIL
    where payment_id = PAY_ID;
    if(countS = 0) then
        DBMS_OUTPUT.PUT_LINE(print_total_sell);
        return print_total_sell;
    end if;
    SELECT PD.payment_id, SUM(PD.AMOUNT * P.product_price) INTO print_payment_id, print_total_sell
    FROM PAYMENT_DETAIL PD, PRODUCT P
    WHERE 
        PD.payment_id = PAY_ID
        AND P.product_id = PD.product_id
    GROUP BY PD.payment_id;
    DBMS_OUTPUT.PUT_LINE(print_total_sell);
    RETURN print_total_sell;
END;
/
----------------------------------------------------------------------------------------
--- Function Tong tien dich vu
CREATE OR REPLACE FUNCTION sumPaymentMenbership(PAY_ID PAYMENT.payment_id%TYPE)
RETURN INT  
AS
    print_payment_id        PAYMENT.payment_id%type;
    print_total_sell        INT := 0;
    countS                  INT := 0;
BEGIN
    select count(*) into countS
    from SIGNUP_MEMBERSHIP
    where payment_id = PAY_ID;
    if(countS = 0) then
        DBMS_OUTPUT.PUT_LINE(print_total_sell);
        return print_total_sell;
    end if;
    SELECT S.payment_id, SUM(M.mem_fee) INTO print_payment_id, print_total_sell
    FROM SIGNUP_MEMBERSHIP S, MEMBERSHIP M
    WHERE 
        S.payment_id = pay_id
        AND M.mem_id = S.mem_id
    GROUP BY S.payment_id;
    DBMS_OUTPUT.PUT_LINE(print_total_sell);
    RETURN print_total_sell;
END;
/
----------------------------------------------------------------------------------------
--- Function tong tien khoa tap
CREATE OR REPLACE FUNCTION sumPaymentClass(PAY_ID PAYMENT.payment_id%TYPE)
RETURN INT  
AS
    print_payment_id        PAYMENT.payment_id%type;
    print_total_sell        INT := 0;
    countS                  INT := 0;
BEGIN
    select count(*) into countS
    from SIGNUP_CLASS
    where payment_id = PAY_ID;
    if(countS = 0) then
        DBMS_OUTPUT.PUT_LINE(print_total_sell);
        return print_total_sell;
    end if;
    SELECT S.payment_id, SUM(C.class_cost) INTO print_payment_id, print_total_sell
    FROM SIGNUP_CLASS S, CLASS C
    WHERE 
        S.payment_id = pay_id
        AND C.class_id = S.class_id
    GROUP BY S.payment_id;
    DBMS_OUTPUT.PUT_LINE(print_total_sell);
    RETURN print_total_sell;
END;
/
----------------------------------------------------------------------------------------
--- Tinh payment total
CREATE OR REPLACE PROCEDURE sumPaymentTotal(PAY_ID PAYMENT.payment_id%TYPE)
IS
    print_cus_type 	    CUSTOMER.cus_type%TYPE;
    print_dis_id		PAYMENT.dis_id%TYPE := 1;
    print_class_total   int;
    print_product_total int;
    print_men_total     int;
    print_discount      INT := 0;
    print_percent       INT := 0;
    SumTotal            INT := 0;
BEGIN        
    SELECT dis_id into print_dis_id
    FROM PAYMENT 
    WHERE payment_id = PAY_ID;

--    EXCEPTION WHEN NO_DATA_FOUND THEN
--    BEGIN
--        DBMS_OUTPUT.PUT_LINE(0);
--    END;
    IF (print_dis_id > 0)
    THEN
        SELECT distinct dis_percent INTO print_discount
        FROM DISCOUNT
        WHERE
            dis_id = print_dis_id;
    ELSE
        print_discount:= 0;
    END IF;
    DBMS_OUTPUT.PUT_LINE(print_discount);
--------------------------------------------------------------------------------------
    SELECT cus_type INTO print_cus_type
    FROM CUSTOMER C, PAYMENT P
    WHERE C.cus_id = P.cus_id AND P.payment_id = PAY_ID;
    
    IF (print_cus_type = 'Thân thiết')
    THEN
        print_percent:= 5;
    ELSIF (print_cus_type = 'VIP')
    THEN
        print_percent:= 15;
    ELSIF (print_cus_type = 'Super VIP')
    THEN
        print_percent:= 25;
    END IF;
    DBMS_OUTPUT.PUT_LINE(print_percent);
--------------------------------------------------------------------------------------

    print_men_total := sumPaymentMenbership(PAY_ID);
    print_product_total := sumPaymentProduct(PAY_ID);
    print_class_total := sumPaymentClass(PAY_ID);
    SumTotal := (print_men_total + print_product_total + print_class_total) * 
                         (1 - print_discount/100) * (1 - print_percent/100);
    DBMS_OUTPUT.PUT_LINE(SumTotal);                    
    UPDATE PAYMENT
    SET total = SumTotal, dis_id = null
    WHERE payment_id = pay_id;
    
    UPDATE PAYMENT
    SET dis_id = null
    WHERE dis_id = print_dis_id;
    
    DELETE FROM DISCOUNT WHERE dis_id = print_dis_id;
    
END;

Begin
    sumPaymentTotal(490);
    sumPaymentTotal(2);
    sumPaymentTotal(3);
    sumPaymentTotal(4);
    sumPaymentTotal(5);
    sumPaymentTotal(6);
    sumPaymentTotal(7);
    sumPaymentTotal(8);
    sumPaymentTotal(9);
    sumPaymentTotal(10);
    sumPaymentTotal(11);
    sumPaymentTotal(12);
    sumPaymentTotal(13);
    sumPaymentTotal(14);
    sumPaymentTotal(15);
    sumPaymentTotal(16);
    sumPaymentTotal(17);
    sumPaymentTotal(18);
    sumPaymentTotal(19);
    sumPaymentTotal(20);
    sumPaymentTotal(21);
    sumPaymentTotal(22);
    sumPaymentTotal(23);
    sumPaymentTotal(24);
    sumPaymentTotal(25);
    sumPaymentTotal(26);
    sumPaymentTotal(27);
    sumPaymentTotal(28);
    sumPaymentTotal(29);
    sumPaymentTotal(30);
    sumPaymentTotal(31);
    sumPaymentTotal(32);
    sumPaymentTotal(33);
    sumPaymentTotal(34);
    sumPaymentTotal(35);
    sumPaymentTotal(36);
    sumPaymentTotal(37);
    sumPaymentTotal(38);
    sumPaymentTotal(39);
    sumPaymentTotal(40);
    sumPaymentTotal(41);
    sumPaymentTotal(42);
    sumPaymentTotal(43);
    sumPaymentTotal(44);
    sumPaymentTotal(45);
    sumPaymentTotal(46);
    sumPaymentTotal(47);
    sumPaymentTotal(48);
    sumPaymentTotal(49);
    sumPaymentTotal(50);
    sumPaymentTotal(51);
    sumPaymentTotal(52);
    sumPaymentTotal(53);
    sumPaymentTotal(54);
    sumPaymentTotal(55);
    sumPaymentTotal(56);
    sumPaymentTotal(57);
    sumPaymentTotal(58);
    sumPaymentTotal(59);
    sumPaymentTotal(60);
End;
BEGIN sumPaymentTotal(270); END;
BEGIN sumPaymentTotal(265); END;

------ Procedure doanh thu khach hang theo thang, nam
CREATE OR REPLACE PROCEDURE pro_revenue_monthly (print_month IN number, print_year IN number)
IS
    print_monthly   number;
    print_yearly    number;
    print_revenue   int;
BEGIN
    SELECT EXTRACT(MONTH FROM payment_lastday), 
            EXTRACT(YEAR FROM payment_lastday), SUM(total)
            INTO print_monthly, print_yearly, print_revenue
    FROM PAYMENT
    WHERE print_month = print_monthly AND print_year = print_yearly
    GROUP BY payment_lastday;
    
    DBMS_OUTPUT.PUT_LINE('Doanh thu tháng ' || print_monthly || '/' || print_yearly || 'là: ' || print_revenue); 
END;

BEGIN
    pro_revenue_monthly(06, 2022);
END;

------ Procedure doanh thu khach hang theo nam
CREATE OR REPLACE PROCEDURE pro_revenue_yearly (print_year IN number)
IS
    print_yearly    number;
    print_revenue   int;
BEGIN
    SELECT EXTRACT (YEAR FROM TO_DATE(payment_lastday, 'DD/MM/YYYY')), SUM(total) INTO print_yearly, print_revenue
    FROM PAYMENT
    WHERE print_year = print_yearly
    GROUP BY payment_lastday;
    
    DBMS_OUTPUT.PUT_LINE('Doanh thu năm ' || print_yearly || 'là: ' || print_revenue); 
END;

------- Procedure LIET KE LICH DA DANG KY CUA CUS theo mã khách hàng
CREATE OR REPLACE PROCEDURE lietKeLichCUS(CUSID IN CUSTOMER.cus_id%TYPE)
AS
    CURSOR cur_customer IS
        SELECT customer.cus_id, cus_name, COUNT(class_id)
        FROM CUSTOMER, SIGNUP_CLASS
        WHERE customer.cus_id = CUSID AND customer.cus_id = signup_class.cus_id
        GROUP BY customer.cus_id, cus_name;
        
    v_id    customer.cus_id%TYPE;
    v_name  customer.cus_name%TYPE;
    v_count number;
    
BEGIN
    OPEN cur_customer;
    LOOP
        FETCH cur_customer INTO v_id, v_name, v_count;
        EXIT WHEN cur_customer%NOTFOUND;
        
        BEGIN
            DBMS_OUTPUT.PUT_LINE('Khách hàng: ' || v_name || ' (Mã khách hàng: ' || v_id || ')');
            DBMS_OUTPUT.PUT_LINE('Số lớp đã đăng ký: ' || v_count);
            DBMS_OUTPUT.PUT_LINE('');
        END;

        DECLARE
        CURSOR cur_signup IS
            SELECT class.class_id, class_period, class_date, class_title, room_id, staff.staff_name, staff_expertise, count(class.class_id)
            FROM staff_class, staff, class, signup_class
            WHERE 
                staff.staff_id = staff_class.staff_id AND 
                class.class_id = staff_class.class_id AND
                class.class_id = signup_class.class_id AND
                signup_class.cus_id = v_id
            GROUP BY class.class_id, class_period, class_date, class_title, room_id, staff.staff_name, staff_expertise;
            
            v_class_id      class.class_id%TYPE;    
            v_period        class.class_period%TYPE;
            v_date          class.class_date%TYPE;
            v_title         class.class_title%TYPE;
            v_room          class.room_id%TYPE;
            v_staff_name    staff.staff_name%TYPE;
            v_expertise     staff.staff_expertise%TYPE;
            v_total         number;
        
            BEGIN
                OPEN cur_signup;
                LOOP
                    FETCH cur_signup INTO v_class_id, v_period, v_date, v_title, v_room, v_staff_name, v_expertise, v_total;
                    EXIT WHEN cur_signup%notfound;
                    IF cur_signup%found THEN
                        BEGIN
                            DBMS_OUTPUT.PUT_LINE('Thông tin các khóa tập khách hàng ' || v_name || ' đã đăng ký: ');
                            DBMS_OUTPUT.PUT_LINE('');
                            DBMS_OUTPUT.PUT_LINE('Mã khóa tập: ' || v_class_id );
                            DBMS_OUTPUT.PUT_LINE('Tên khóa: ' || v_title);
                            DBMS_OUTPUT.PUT_LINE('Thời gian: ' || v_period);
                            DBMS_OUTPUT.PUT_LINE('Ngày khai giảng: ' || v_date);
                            DBMS_OUTPUT.PUT_LINE('Môn: ' || v_expertise);
                            DBMS_OUTPUT.PUT_LINE('Phòng: ' || v_room);                   
                            DBMS_OUTPUT.PUT_LINE('Huấn luyện viên: ' || v_staff_name);
                            DBMS_OUTPUT.PUT_LINE('Sỉ số: ' || v_total);
                        END;
                    END IF;
                END LOOP;
                CLOSE cur_signup;
            END;
    END LOOP;
    CLOSE cur_customer;
END;

------- Procedure LIET KE LICH DA DANG KY CUA CUS theo số điện thoại
CREATE OR REPLACE PROCEDURE lietKeLichCUSSDT(SDT IN CUSTOMER.cus_telephone%TYPE)
AS
    CURSOR cur_customer IS
        SELECT customer.cus_id, cus_name, COUNT(class_id)
        FROM CUSTOMER, SIGNUP_CLASS
        WHERE customer.cus_telephone = SDT AND customer.cus_id = signup_class.cus_id
        GROUP BY customer.cus_id, cus_name;
        
    v_id    customer.cus_id%TYPE;
    v_name  customer.cus_name%TYPE;
    v_count number;
    
BEGIN
    OPEN cur_customer;
    LOOP
        FETCH cur_customer INTO v_id, v_name, v_count;
        EXIT WHEN cur_customer%NOTFOUND;
        
        BEGIN
            DBMS_OUTPUT.PUT_LINE('Khách hàng: ' || v_name || ' (Mã khách hàng: ' || v_id || ')');
            DBMS_OUTPUT.PUT_LINE('Số lớp đã đăng ký: ' || v_count);
            DBMS_OUTPUT.PUT_LINE('');
        END;

        DECLARE
        CURSOR cur_signup IS
            SELECT class.class_id, class_period, class_date, class_title, room_id, staff.staff_name, staff_expertise, count(class.class_id)
            FROM staff_class, staff, class, signup_class
            WHERE 
                staff.staff_id = staff_class.staff_id AND 
                class.class_id = staff_class.class_id AND
                class.class_id = signup_class.class_id AND
                signup_class.cus_id = v_id
            GROUP BY class.class_id, class_period, class_date, class_title, room_id, staff.staff_name, staff_expertise;
            
            v_class_id      class.class_id%TYPE;    
            v_period        class.class_period%TYPE;
            v_date          class.class_date%TYPE;
            v_title         class.class_title%TYPE;
            v_room          class.room_id%TYPE;
            v_staff_name    staff.staff_name%TYPE;
            v_expertise     staff.staff_expertise%TYPE;
            v_total         number;
        
            BEGIN
                OPEN cur_signup;
                LOOP
                    FETCH cur_signup INTO v_class_id, v_period, v_date, v_title, v_room, v_staff_name, v_expertise, v_total;
                    EXIT WHEN cur_signup%notfound;
                    IF cur_signup%found THEN
                        BEGIN
                            DBMS_OUTPUT.PUT_LINE('Thông tin các khóa tập khách hàng ' || v_name || ' đã đăng ký: ');
                            DBMS_OUTPUT.PUT_LINE('');
                            DBMS_OUTPUT.PUT_LINE('Mã khóa tập: ' || v_class_id );
                            DBMS_OUTPUT.PUT_LINE('Tên khóa: ' || v_title);
                            DBMS_OUTPUT.PUT_LINE('Thời gian: ' || v_period);
                            DBMS_OUTPUT.PUT_LINE('Ngày khai giảng: ' || v_date);
                            DBMS_OUTPUT.PUT_LINE('Môn: ' || v_expertise);
                            DBMS_OUTPUT.PUT_LINE('Phòng: ' || v_room);                   
                            DBMS_OUTPUT.PUT_LINE('Huấn luyện viên: ' || v_staff_name);
                            DBMS_OUTPUT.PUT_LINE('Sỉ số: ' || v_total);
                        END;
                    END IF;
                END LOOP;
                CLOSE cur_signup;
            END;
    END LOOP;
    CLOSE cur_customer;
END;


BEGIN
    lietKeLichCUS('1');
END;

--DECLARE
--    MaKH number;
--BEGIN
--    MaKH:=&MaKH;
--    lietKeLichCUS(MaKH);
--END;

------- Procedure LIET KE LICH DA DANG KY CUA STAFF
CREATE OR REPLACE PROCEDURE lietKeLichSTAFF(STAFFID IN staff.staff_id%TYPE)
AS
    CURSOR cur_staff IS
        SELECT staff_id, staff_name
        FROM STAFF
        WHERE staff_id = STAFFID;
    v_id    staff.staff_id%TYPE;
    v_name  staff.staff_name%TYPE;
    
BEGIN
    OPEN cur_staff;
    LOOP
        FETCH cur_staff INTO v_id, v_name;
        EXIT WHEN cur_staff%NOTFOUND;
        
        dbms_output.put_line('Huấn luyện viên: ' || v_name || '(Mã HLV: ' || v_id || ')');
        
        DECLARE
        CURSOR cur_staff_class IS
            SELECT class.class_id, class_period, class_date, room_id, class_cost, class_title, COUNT(cus_id)
            FROM staff_class, class, signup_class
            WHERE 
                class.class_id = staff_class.class_id
                AND signup_class.class_id = class.class_id
                AND v_id = staff_class.staff_id
            GROUP BY class.class_id, class_period, class_date, room_id, class_cost, class_title;
                
        v_class     class.class_id%TYPE;
        v_period    class.class_period%TYPE;
        v_date      class.class_date%TYPE;
        v_room      class.room_id%TYPE;
        v_cost      class.class_cost%TYPE;
        v_title     class.class_title%TYPE;
        v_total     NUMBER;
        
        BEGIN
            OPEN cur_staff_class;
            LOOP
                FETCH cur_staff_class INTO v_class, v_period, v_date, v_room, v_cost, v_title, v_total;
                EXIT WHEN cur_staff_class%notfound;
                IF cur_staff_class%found
                THEN
                    BEGIN
                        DBMS_OUTPUT.PUT_LINE('Thông tin các khóa huấn luyện viên ' || v_name || ' đã đăng ký: ');
                        DBMS_OUTPUT.PUT_LINE('');
                        DBMS_OUTPUT.PUT_LINE('Mã khóa tập: ' || v_class);
                        DBMS_OUTPUT.PUT_LINE('Tên khóa: ' || v_title);
                        DBMS_OUTPUT.PUT_LINE('Thời gian: ' || v_period);
                        DBMS_OUTPUT.PUT_LINE('Ngày khai giảng: ' || v_date);
                        DBMS_OUTPUT.PUT_LINE('Phòng: ' || v_room);                   
                        DBMS_OUTPUT.PUT_LINE('Sỉ số: ' || v_total);
                    END;
                END IF;
            END LOOP;
            CLOSE cur_staff_class;
        END;  
    END LOOP;
    CLOSE cur_staff;
END;

-- Procedure In thong tin nhan vien
CREATE OR REPLACE PROCEDURE pro_staff (print_staff_id IN staff.staff_id%type)
AS
    print_name        staff.staff_name%type;
    print_telephone   staff.staff_telephone%type;
    print_salary      staff.staff_salary%type;
BEGIN
    SELECT staff_name, staff_telephone, staff_salary INTO print_name, print_telephone, print_salary
    FROM STAFF
    WHERE staff_id = print_staff_id;
    DBMS_OUTPUT.PUT_LINE('Thông tin nhân viên ' || print_staff_id);
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('HỌ TÊN: '|| print_name ||' -- SĐT: '|| print_telephone || ' -- Lương: '|| print_salary);
end;

BEGIN
    pro_staff(1);
END;

---------------------------
--------- TRIGGER ---------
---------------------------
SET SERVEROUTPUT ON;

-- Trigger 1: Tang cus_revenue
------- Khi Hóa đơn của khách hàng thay đổi thì tổng chi  tiêu của khách hàng cũng đổi theo
CREATE OR REPLACE TRIGGER TRIGGER_CUS_REVENUE_increased
AFTER INSERT OR UPDATE OR DELETE ON payment
FOR EACH ROW
BEGIN
    IF(INSERTING) THEN
        UPDATE CUSTOMER
        SET cus_revenue = (cus_revenue + :NEW.total)
        WHERE cus_id = :new.cus_id;
    END IF; 
    IF(UPDATING) THEN
        UPDATE CUSTOMER
        SET cus_revenue = (cus_revenue + (:NEW.total - :old.total))
        WHERE cus_id = :new.cus_id;
    END IF; 
    IF(DELETING) THEN
        UPDATE CUSTOMER
        SET cus_revenue = (cus_revenue - :old.total)
        WHERE cus_id = :old.cus_id;   
    END IF;  
END;
/

DROP TRIGGER TRIGGER_CUS_REVENUE_increased;
SET DEFINE OFF;

-- Trigger 2: 
------ Khi lap hoa don, nguoi co chuc vu 'Huan luyen vien' khong duoc phep thuc hien.
CREATE OR REPLACE TRIGGER TRIGGER_PAYMENT_STAFF
BEFORE INSERT OR UPDATE ON PAYMENT
FOR EACH ROW
DECLARE
    v_type  staff.staff_type%TYPE;
    v_staff payment.staff_id%TYPE;
BEGIN
    SELECT staff.staff_id, staff_type INTO V_STAFF, v_type
    FROM staff
    WHERE staff_id=:new.staff_id;
    
    IF (v_type != 'Huấn luyện viên' OR v_type IS NULL)
    THEN
        DBMS_OUTPUT.PUT_LINE('Lập hóa đơn thành công!');
    ELSE
        RAISE_APPLICATION_ERROR(-20000, 'Người lập hóa đơn không hợp lệ!!!');
    END IF;
END;

DROP TRIGGER TRIGGER_PAYMENT_STAFF;
SET DEFINE OFF;

-- Trigger 3:
-------- Khi dang ky khoa tap, chi co nhan vien thuoc loai 'Huấn luyện viên' moi duoc phep thuc hien.
CREATE OR REPLACE TRIGGER TRIGGER_CLASS_STAFF
AFTER INSERT OR UPDATE ON STAFF_CLASS
FOR EACH ROW
DECLARE
    v_type staff.staff_type%TYPE;
BEGIN
    SELECT staff.staff_type INTO v_type
    FROM staff
    WHERE 
            staff_id=:NEW.staff_id ;
    
    IF (v_type = 'Huấn luyện viên')
    THEN
        DBMS_OUTPUT.PUT_LINE('Mở khóa tập thành công!');
    ELSE
        RAISE_APPLICATION_ERROR(-20000, 'Người mở khóa tập không phải là huấn luyện viên!!!');
    END IF;
END;

DROP TRIGGER TRIGGER_CLASS_STAFF;
SET DEFINE OFF;


-- Trigger 4:
-------- Ngay sinh cua khach hang phai nho hon ngay lap hoa don
CREATE OR REPLACE TRIGGER TRIGGER_PAYMENT_CUSTOMER_DATE
AFTER UPDATE OF CUS_BIRTHDAY ON CUSTOMER
FOR EACH ROW
DECLARE
    CURSOR cur_payment IS 
        SELECT payment_id, payment_lastday
        FROM payment
        WHERE cus_id =:NEW.cus_id;
        
    v_payment payment.payment_id%type;
    v_date payment.payment_lastday%TYPE;
    
BEGIN
    OPEN cur_payment;
    LOOP
        FETCH cur_payment INTO v_payment, v_date;
        EXIT WHEN cur_payment%NOTFOUND;
        
        IF(:NEW.cus_birthday > v_date)
        THEN
            RAISE_APPLICATION_ERROR(-20002, 'Ngày sinh của khách hàng phải nhỏ hơn ngày hóa đơn đã lập');
        ELSE 
            DBMS_OUTPUT.PUT_LINE('Sửa khách hàng thành công!');
        END IF;
    END LOOP;
    CLOSE cur_payment;
END;
/

DROP TRIGGER TRIGGER_payment_CUSTOMER_DATE;
SET DEFINE OFF;

-- Trigger 5: 
-------- Ngay sinh cua nhan vien phai nho hon ngay lap hoa don
CREATE OR REPLACE TRIGGER TRIGGER_payment_STAFF_DATE
AFTER UPDATE OF STAFF_BIRTHDAY ON STAFF
FOR EACH ROW
DECLARE
    CURSOR cur_payment IS 
        SELECT payment_id, payment_lastday
        FROM payment
        WHERE staff_id =:NEW.staff_id;
        
    v_payment payment.payment_id%type;
    v_date payment.payment_lastday%TYPE;
BEGIN
    OPEN cur_payment;
    LOOP
        FETCH cur_payment INTO v_payment, v_date;
        EXIT WHEN cur_payment%NOTFOUND;

        IF(:NEW.staff_birthday > v_date)
        THEN
            RAISE_APPLICATION_ERROR(-20002, 'Ngày sinh của nhân viên phải nhỏ hơn ngày lập hóa đơn');
        ELSE 
            DBMS_OUTPUT.PUT_LINE('Sửa nhân viên thành công');
        END IF;
    END LOOP;
END;
/

DROP TRIGGER TRIGGER_payment_STAFF_DATE;
SET DEFINE OFF;

-- Trigger 6:
-------- Ngay lap hoa don phai sau ngay tham gia cua khach hang
CREATE OR REPLACE TRIGGER TRIGGER_CUSTOMER_payment_DATE
AFTER INSERT OR UPDATE OF payment_lastday ON payment
FOR EACH ROW
DECLARE
    v_cus_datejoin customer.cus_datejoin%TYPE;
BEGIN
    SELECT customer.cus_datejoin INTO v_cus_datejoin
    FROM customer
    WHERE customer.cus_id =:NEW.cus_id;
    IF(:NEW.payment_lastday < v_cus_datejoin)
    THEN
        RAISE_APPLICATION_ERROR(-20004, 'Ngày lập hóa đơn không hợp lệ!!!');
    END IF;
END;
/

SET DEFINE OFF;
DROP TRIGGER TRIGGER_CUSTOMER_payment_DATE;

-- Trigger 7: 
-------- Ngày vao lam cua nhan vien phai som hon ngay lap hoa don
CREATE OR REPLACE TRIGGER TRIGGER_STAFF_payment_DATE
AFTER INSERT OR UPDATE OF payment_lastday ON payment
FOR EACH ROW
DECLARE
    v_staff_datejoin staff.staff_datejoin%TYPE;
BEGIN
    SELECT staff.staff_datejoin INTO v_staff_datejoin
    FROM staff
    WHERE staff.staff_id =:NEW.staff_id;
    IF(:NEW.payment_lastday < v_staff_datejoin)
    THEN
        RAISE_APPLICATION_ERROR(-20004, 'Ngày lập hóa đơn không hợp lệ!!!');
    END IF;
END;
/

SET DEFINE OFF;
DROP TRIGGER TRIGGER_STAFF_payment_DATE;

-- Trigger 8: 
-------- Cập nhập loại khách hàng theo chi tiêu của khách hàng
CREATE OR REPLACE TRIGGER TRIGGER_CUS_TYPE_NEW
BEFORE UPDATE ON customer
FOR EACH ROW
BEGIN  
    IF (:new.cus_revenue <= 5000000)
    THEN
        :new.cus_type := 'Thân thiết';
    END IF;
    IF (:new.cus_revenue > 5000000 and :new.cus_revenue <= 15000000)
    THEN
        :new.cus_type := 'VIP';
    END IF;
    IF (:new.cus_revenue > 15000000)
    THEN
        :new.cus_type := 'Super VIP';
    END IF;
END;
/


-- Trigger 9: 
-------- Khi khach hang mua san pham thi so luong san pham se giam xuong.
CREATE OR REPLACE TRIGGER Trigger_Decrease_product
AFTER INSERT OR UPDATE ON PAYMENT_DETAIL
FOR EACH ROW 
DECLARE
    print_id        PRODUCT.product_id%TYPE;
    print_amount    PRODUCT.product_amount%TYPE;
BEGIN
    SELECT product.product_id, product_amount INTO print_id, print_amount
    FROM  product
    WHERE product_id=:new.product_id;
    
    IF(print_amount = 0)
    THEN 
        RAISE_APPLICATION_ERROR(-20009,'Không còn hàng để bán');
    ELSE
        UPDATE PRODUCT
        SET product_amount = product_amount - 1
        WHERE product_id = print_id;
    END IF;
END;
/

-- Trigger 10: Huan luyen vien co the dang ky toi da 2 lop trong 1 thang
CREATE OR REPLACE TRIGGER trigger_check_staff_class
BEFORE INSERT OR UPDATE ON staff_class
FOR EACH ROW
DECLARE
    v_class         number;
    v_count_staff   Staff_CLASS.staff_id%TYPE;
    v_date          CLASS.class_date%TYPE;
    v_month         number;
    v_year          number;
BEGIN
    
    SELECT class_date into v_date
    FROM CLASS
    WHERE class_id=:new.class_id;
    
    IF (v_date IS NOT NULL)
    THEN
        v_month := EXTRACT (MONTH FROM TO_DATE(v_date, 'DD/MM/YYYY'));
        v_year := EXTRACT (YEAR FROM TO_DATE(v_date, 'DD/MM/YYYY'));
        
        SELECT class_id, COUNT(*) INTO v_class, v_count_staff
        FROM CLASS
        WHERE class_id=:new.class_id AND 
                EXTRACT (MONTH FROM TO_DATE(class_date, 'DD/MM/YYYY')) = v_month AND
                EXTRACT (YEAR FROM TO_DATE(class_date, 'DD/MM/YYYY')) = v_year
        GROUP BY class_id;
        
        IF (v_count_staff > 2)
        THEN
            RAISE_APPLICATION_ERROR(-20090, 'Huấn luyện viên chỉ được đăng ký tối đa 2 lớp/tháng!');
        ELSE
            DBMS_OUTPUT.PUT_LINE('Đăng ký lớp thành công!');
        END IF;
        
    END IF;
END;
/

-- Trigger 11: Tu dong cap nhat chieu cao va can nang cua customer sau khi inbody
CREATE OR REPLACE TRIGGER trigger_weight_height
AFTER INSERT OR UPDATE OF weight,height ON inbody
FOR EACH ROW
BEGIN
    UPDATE CUSTOMER
    SET cus_weight = :new.weight ,cus_height =:new.height
    WHERE cus_id =:new.cus_id;
END;
/

--Trigger 12: Các class không được trùng giờ nhau giữa các HLV
CREATE OR REPLACE TRIGGER TRIGGER_CLASS_STAFF
BEFORE INSERT OR UPDATE ON STAFF_CLASS
FOR EACH ROW
DECLARE
	sumS number := 0;
	cDate CLASS.class_date%TYPE;
	cPeriod CLASS.class_period%TYPE;
BEGIN
	SELECT class_date, class_period INTO cDate, cPeriod
	FROM CLASS
	WHERE class_id = :new.class_id;

	SELECT COUNT(*) INTO sumS
	FROM STAFF_CLASS S, CLASS C
	WHERE S.class_id = C.class_id AND S.staff_id = :new.staff_id AND C.class_period = cPeriod AND C.class_date = cDate;
	
	IF(sumS = 0)
	THEN
      	DBMS_OUTPUT.PUT_LINE('Đăng ký thành công !');
    	ELSE
      	RAISE_APPLICATION_ERROR(-20000, 'Đã có lịch trong thời gian này!');
    	END IF;
END;
/


-- Trigger 13: Các class không được trùng giờ nhau giữa các customer
CREATE OR REPLACE TRIGGER TRIGGER_SIGNUP_CLASS
BEFORE INSERT OR UPDATE ON SIGNUP_CLASS
FOR EACH ROW
DECLARE
	sumS number := 0;
	cDate CLASS.class_date%TYPE;
	cPeriod CLASS.class_period%TYPE;
BEGIN
	SELECT class_date, class_period INTO cDate, cPeriod
	FROM CLASS
	WHERE class_id = :new.class_id;

	SELECT COUNT(*) INTO sumS
	FROM SIGNUP_CLASS S, CLASS C
	WHERE S.class_id = C.class_id AND S.cus_id = :new.cus_id AND C.class_period = cPeriod AND C.class_date = cDate;
	
	IF(sumS = 0)
	THEN
      	DBMS_OUTPUT.PUT_LINE('Đăng ký thành công !');
    	ELSE
      	RAISE_APPLICATION_ERROR(-20000, 'Đã có lịch trong thời gian này!');
    	END IF;
END;
/

-- Trigger 14: Các class không được trùng giờ nhau giữa các phòng
CREATE OR REPLACE TRIGGER TRIGGER_SIGNUP_CLASS_TIME
BEFORE INSERT ON CLASS
FOR EACH ROW
DECLARE
	sumR number := 0;
BEGIN
	SELECT COUNT(*) INTO sumR
	FROM CLASS
	WHERE class_period = :new.class_period AND class_date = :new.class_date AND room_id = :new.room_id;

	
	IF(sumR = 0)
	THEN
      	DBMS_OUTPUT.PUT_LINE('Tạo class thành công !');
    	ELSE
      	RAISE_APPLICATION_ERROR(-20000, 'Đã có lịch trong thời gian này!');
    	END IF;
END;
/

-- Trigger 15: Giới hạn customer đăng ký vào 1 lớp.
CREATE OR REPLACE TRIGGER TRIGGER_SIGNUP_CLASS_CUSTOMER
BEFORE INSERT OR UPDATE ON SIGNUP_CLASS
FOR EACH ROW
DECLARE
	sumM number := 0;
	maxRoom CLASS.room_id%TYPE := 0;
BEGIN
	SELECT R.room_capacity INTO maxRoom
	FROM CLASS C, ROOM R
	WHERE C.room_id = R.room_id AND C.class_id = :new.class_id;
	
	SELECT COUNT(*) INTO sumM
	FROM SIGNUP_CLASS
	WHERE class_id = :new.class_id;
	
	IF(sumM < maxRoom)
	THEN
      	DBMS_OUTPUT.PUT_LINE('Đăng ký thành công !');
    	ELSE
      	RAISE_APPLICATION_ERROR(-20000, 'Vượt quá sức chứa của phòng này!');
    	END IF;
END;
/

-- Trigger 16: Rang buoc ngay dang ky membership, class, payment_lastday phai nho hon ngay het han cus_memexpired
CREATE OR REPLACE TRIGGER TRIGGER_PAYMENT_CUSTOMER_DATE
AFTER UPDATE OF CUS_BIRTHDAY ON CUSTOMER
FOR EACH ROW
DECLARE
    CURSOR cur_payment IS 
        SELECT payment_id, payment_lastday
        FROM payment
        WHERE cus_id =:NEW.cus_id;
        
    v_payment payment.payment_id%type;
    v_date payment.payment_lastday%TYPE;
    
BEGIN
    OPEN cur_payment;
    LOOP
        FETCH cur_payment INTO v_payment, v_date;
        EXIT WHEN cur_payment%NOTFOUND;
        
        IF(:NEW.cus_birthday > v_date)
        THEN
            RAISE_APPLICATION_ERROR(-20002, 'Ngày sinh của khách hàng phải nhỏ hơn ngày hóa đơn đã lập');
        ELSE 
            DBMS_OUTPUT.PUT_LINE('Sửa khách hàng thành công!');
        END IF;
    END LOOP;
    CLOSE cur_payment;
END;
/

-- Trigger 17: Cap nhat ngay het han
CREATE OR REPLACE TRIGGER TRIGGER_cus_memexpired
AFTER INSERT ON SIGNUP_MEMBERSHIP
FOR EACH ROW
DECLARE
    ex date;
BEGIN
    select cus_memexpired into ex
    from customer
    where cus_id = :new.cus_id;
    
    if(ex is null) then
        update customer
        set cus_memexpired = ADD_MONTHS(:new.SIGNUP_DATE, 1)
        where cus_id = :new.cus_id;
    end if;
    if(ex >= CURRENT_DATE) then
        update customer
        set cus_memexpired = ADD_MONTHS(cus_memexpired, 1)
        where cus_id = :new.cus_id;    
    end if;
    if(ex < CURRENT_DATE) then
        update customer
        set cus_memexpired = ADD_MONTHS(:new.SIGNUP_DATE, 1)
        where cus_id = :new.cus_id;
    end if;    
    
END;
/

-- Trigger 18: Tang san pham khi xoa hoa don
create or replace trigger Trigger_Increase_Product
AFTER DELETE ON PAYMENT_DETAIL
FOR EACH ROW
DECLARE 
    print_id        PRODUCT.product_id%TYPE;
    print_amount    PRODUCT.product_amount%TYPE;
BEGIN
    SELECT product.product_id, product_amount INTO print_id, print_amount
    FROM  product
    WHERE product_id=:old.product_id;
    
    UPDATE PRODUCT
    SET product_amount = product_amount + print_amount
    WHERE product_id = print_id;
END;
/

-----------------------------------------------------
------ GIAI QUYET VAN DE TRUY XUAT DONG THOI --------
-----------------------------------------------------

------ CÁCH KHẮC PHỤC: THIẾT LẬP MỨC CÔ LẬP TUẦN TỰ (SET TRANSACTION ISOLATION LEVEL Serializable;)

SET SERVEROUTPUT ON;

-- PROCEDURE Sleep --
CREATE OR REPLACE PROCEDURE sleep (in_time number)
AS
    v_current date;
BEGIN
    SELECT SYSDATE INTO v_current
    FROM DUAL;
    LOOP
    EXIT WHEN v_current + (in_time * (1/86400)) <= SYSDATE;
    END LOOP;
end;

    ------------------------
    ----- LOST UPDATE ------
    ------------------------
    
-- Tình huống: một nhân viên đang thay đổi thông tin sản phẩm thì có nhân viên khác cũng vào thay đổi thông tin sản phẩm.

-- Minh họa tình huống: 
--------- TRANSACTION 1: 
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định
BEGIN
    pro_product(1); --- PRODUCT_PRICE = 120000
END;
UPDATE PRODUCT SET PRODUCT_PRICE = 200000 WHERE PRODUCT_ID = 1; --- 1 row updated

--------- TRANSACTION 2: 
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định
BEGIN
    pro_product(1); --- PRODUCT_PRICE = 120000
END;
UPDATE PRODUCT SET PRODUCT_PRICE = 180000 WHERE PRODUCT_ID = 1; --- Chờ vô hạn thời gian.

/* TRANSACTION 1 */ COMMIT; /* -----> TRANSACTION 2: 1 row updated */ COMMIT;

/* KẾT QUẢ */
--------- TRANSACTION 1:
BEGIN
    pro_product(1);
END; 
-- =========> PRODUCT_PRICE = 180000 (mất dữ liệu khi update product_price = 200000)

/* Giải quyết tình huống: */
BEGIN
    pro_product(1);
END;
BEGIN
    SET TRANSACTION ISOLATION LEVEL Serializable;
    UPDATE PRODUCT SET PRODUCT_PRICE = 200000 WHERE PRODUCT_ID = 1;
    SLEEP(10);
    COMMIT;
END;

------------------------------------------------------------------------

-- T1:
BEGIN
    pro_product(1);
END;
UPDATE PRODUCT SET PRODUCT_PRICE = 200000 WHERE PRODUCT_ID=1;	

-- T2:
	SELECT * FROM PRODUCT WHERE PRODUCT_ID  = 1 FOR UPDATE;
    COMMIT;
	UPDATE PRODUCT SET PRODUCT_PRICE = 180000 WHERE PRODUCT_ID = 1;
	COMMIT;


------------------------------------------------------------------------
-- T1:
SELECT * FROM PRODUCT WHERE PRODUCT_ID =1;	
UPDATE PRODUCT SET PRODUCT_PRICE = 200000, VERSION = 2 WHERE PRODUCT_ID = 1 AND VERSION = 1;	

-- T2:
	SELECT * FROM PRODUCT WHERE PRODUCT_ID  = 1
	UPDATE PRODUCT SET PRODUCT_PRICE = 180000, VERSION = 2 WHERE PRODUCT_ID = 1 AND VERSION = 1;
COMMIT;	
	ROLLBACK;
    

------------------------------------------------------------------------
-- Minh họa tình huống: 
--------- TRANSACTION 1: 
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định
SELECT DISTINCT PRODUCT_AMOUNT FROM PRODUCT JOIN PAYMENT_DETAIL ON PRODUCT.PRODUCT_ID = PAYMENT_DETAIL.PRODUCT_ID 
WHERE PRODUCT.PRODUCT_ID = 18;
BEGIN
    INSERT_PAYMENT('15/06/2022', 'Tiền mặt', 24, 441,  null);
END;

BEGIN
    Insert_PAYMENT_DETAIL(632, 18, 1);
END;

SELECT DISTINCT PRODUCT_AMOUNT FROM PRODUCT JOIN PAYMENT_DETAIL ON PRODUCT.PRODUCT_ID = PAYMENT_DETAIL.PRODUCT_ID 
WHERE PRODUCT.PRODUCT_ID = 18;

COMMIT;

ROLLBACK;

--------- TRANSACTION 2: 
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định

BEGIN
    INSERT_PAYMENT('15/06/2022', 'Tiền mặt', 25, 461,  null);
END;

BEGIN
    Insert_PAYMENT_DETAIL(633, 18, 2);
END;

SELECT DISTINCT PRODUCT_AMOUNT FROM PRODUCT JOIN PAYMENT_DETAIL ON PRODUCT.PRODUCT_ID = PAYMENT_DETAIL.PRODUCT_ID 
WHERE PRODUCT.PRODUCT_ID = 18;

COMMIT;

ROLLBACK;

/* Giải quyết tình huống: */
-- TRANSACTION 1:
SELECT DISTINCT PRODUCT_AMOUNT FROM PRODUCT JOIN PAYMENT_DETAIL ON PRODUCT.PRODUCT_ID = PAYMENT_DETAIL.PRODUCT_ID 
WHERE PRODUCT.PRODUCT_ID = 18;

BEGIN
    SET TRANSACTION ISOLATION LEVEL Serializable;
    BEGIN
        INSERT_PAYMENT('15/06/2022', 'Tiền mặt', 24, 441,  null);
    END;
    
    BEGIN
        Insert_PAYMENT_DETAIL(632, 18, 1);
    END;    
    SLEEP(10);
    COMMIT;
END;





    -------------------------
    ----- PHANTOM READ ------
    -------------------------

-- Tình huống: Một nhân viên đang xem số lượng các sản phẩm của phòng tập thì có một nhân viên khác thêm vào một sản phẩm mới. 
--Nhân viên xem lại một lần nữa thì thấy nhiều hơn dữ liệu.

-- Minh họa tình huống: 
--------- TRANSACTION 1: 
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định
SELECT * FROM PRODUCT; --- Hiển thị danh sách n sản phẩm

--------- TRANSACTION 2: 
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định
BEGIN
    INSERT_PRODUCT('ĐAI BẢO VỆ LƯNG',150000,'Dụng cụ luyện tập',20);
    INSERT_PRODUCT('BAO TAY MA SÁT', 50000,'Dụng cụ luyện tập',10);
END;
COMMIT;--- Thêm 2 sản phẩm và đã commit

/* KẾT QUẢ */
--------- TRANSACTION 1:
SELECT * FROM PRODUCT; 
COMMIT;
--- Hiển thị danh sách n+2 sản phẩm.

/* Giải quyết tình huống: */
SELECT * FROM PRODUCT;
BEGIN
    SET TRANSACTION ISOLATION LEVEL Serializable;
    INSERT_PRODUCT('ĐAI BẢO VỆ LƯNG',150000,'Dụng cụ luyện tập',20);
    INSERT_PRODUCT('BAO TAY MA SÁT', 50000,'Dụng cụ luyện tập',10);    
    SLEEP(10);
    COMMIT;
END;

-- hoặc:

SELECT * FROM PRODUCT;
BEGIN
    SET TRANSACTION ISOLATION LEVEL Read committed;
    INSERT_PRODUCT('ĐAI BẢO VỆ LƯNG',150000,'Dụng cụ luyện tập',20);
    INSERT_PRODUCT('BAO TAY MA SÁT', 50000,'Dụng cụ luyện tập',10);
    LOCK TABLE PRODUCT IN EXCLUSIVE MODE;
    SLEEP(10);
    COMMIT;
END;

-------------------------------------------------------------------------------
-- T1:
SELECT COUNT(*) FROM CLASS	

-- T2:
	BEGIN
    INSERT_CLASS('17h30 - 19h00', '01/05/2022', 'Boxing cho người mới' , 5000000, 3);
    END;
	COMMIT;

-- T1
SELECT COUNT(*) FROM CLASS	
COMMIT;	

-------------------------------------------------------------------------------
-- TRANSACTION 1:
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định

SELECT CLASS_ID, COUNT(*) AS SLHV
FROM signup_class
WHERE CLASS_ID = 18
GROUP BY CLASS_ID;

COMMIT;

-- TRANSACTION 2:
BEGIN
    INSERT_PAYMENT('18/06/2022', 'Thẻ tín dụng', 24, 441, null);
END;

BEGIN
    Insert_SIGNUP_CLASS(751, 441, 18, '18/06/2022');
END;

-- TRANSACTION 1:
SELECT CLASS_ID, COUNT(*) AS SLHV
FROM signup_class
WHERE CLASS_ID = 18
GROUP BY CLASS_ID;

COMMIT;


    ------------------------------
    ----- NON-REPEATABLE READ ------
    ------------------------------
    
-- Tình huống: Một khách hàng đang xem thông tin của sản phẩm thì có một nhân viên vào thay đổi thông tin của sản phẩm. 
--Khách hàng xem thông tin sản phẩm một lần nữa thì thấy có sự thay đổi về dữ liệu.
    
-- Minh họa tình huống: 
--------- TRANSACTION 1: 
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định
BEGIN
    pro_product(1);
END; --- Tên SP: Túi tập nhỏ ADIDAS, SL = 20
COMMIT;

--------- TRANSACTION 2:
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định
UPDATE PRODUCT SET PRODUCT_NAME = ‘Túi tập nhỏ Nike’, SL = 30 WHERE PRODUCT_ID = 1; 
COMMIT;

/* KẾT QUẢ */
--------- TRANSACTION 1: 
BEGIN
    pro_product(1); --- PRODUCT_PRICE = 120000
    SLEEP(10);
END; --- Tên SP: Túi tập nhỏ NIKE, SL = 30
COMMIT;

/* Giải quyết tình huống: */
SET TRANSACTION ISOLATION LEVEL Serializable; --- Thiết lập mức cô lập mặc định
BEGIN
    pro_product(1);
END; --- Tên SP: Túi tập nhỏ ADIDAS, SL = 20
COMMIT;

    ---------------------
    ----- DEADLOCK ------
    ---------------------
    
-- Tình huống: Một nhân viên update thông tin sản phẩm mang mã là 2 nhưng chưa COMMIT, 
--nhân viên 2 update thông tin của sản phẩm mang mã là 3 nhưng chưa COMMIT. 
--Sau đó nhân viên 2 lại update thông tin của sản phẩm mang mã là 3 còn nhân viên 2 lại update thông tin của sản phẩm mang mã là 2 
-- => Xuất hiện deadlock

-- Minh họa tình huống: 
--------- TRANSACTION 1: 
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định
    UPDATE PRODUCT SET PRODUCT_PRICE = 150000 WHERE PRODUCT_ID = 2; -- 1 row updated (thứ tự thực hiện: 1)
    UPDATE PRODUCT SET PRODUCT_AMOUNT = 30 WHERE PRODUCT_ID = 3; -- ORA-00060: Deadlock detected (thứ tự thực hiện: 3)

--------- TRANSACTION 2: 
SET TRANSACTION ISOLATION LEVEL Read committed; --- Thiết lập mức cô lập mặc định
BEGIN
    UPDATE PRODUCT SET PRODUCT_PRICE = 250000 WHERE PRODUCT_ID = 3; -- 1 row updated (thứ tự thực hiện: 2)
    SLEEP(15);
END;
    UPDATE PRODUCT SET PRODUCT_AMOUNT = 50 WHERE PRODUCT_ID = 2; -- Waiting (thứ tự thực hiện: 4)
    COMMIT;
    
/* Giải quyết tình huống: ORACLE tự ROLLBACK TRANSACTION bị lỗi */