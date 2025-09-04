-- 회원 테이블 생성
CREATE TABLE members (
    id NUMBER(19) PRIMARY KEY,
    username VARCHAR2(50) NOT NULL UNIQUE,
    email VARCHAR2(100) NOT NULL,
    name VARCHAR2(100) NOT NULL,
    created_at DATE DEFAULT SYSDATE,
    updated_at DATE DEFAULT SYSDATE
);

-- 시퀀스 생성
CREATE SEQUENCE members_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- 트리거 생성 (ID 자동 생성)
CREATE OR REPLACE TRIGGER members_trigger
    BEFORE INSERT ON members
    FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := members_seq.NEXTVAL;
    END IF;
END;
/

-- 샘플 데이터 삽입
INSERT INTO members (username, email, name) VALUES ('admin', 'admin@billyeogayu.com', '관리자');
INSERT INTO members (username, email, name) VALUES ('user1', 'user1@billyeogayu.com', '사용자1');
INSERT INTO members (username, email, name) VALUES ('user2', 'user2@billyeogayu.com', '사용자2');

COMMIT;