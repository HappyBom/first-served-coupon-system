CREATE TABLE coupons (
      id                BIGSERIAL PRIMARY KEY
    , title             VARCHAR(255) NOT NULL
    , coupon_gubun      VARCHAR(255) NOT NULL
    , total_cnt       	INT NULL
    , issue_cnt      	INT NOT NULL
    , discount_amt      INT NOT NULL
    , min_avail_amt 	INT NOT NULL
    , issue_st     		TIMESTAMP NOT NULL
    , issue_end       	TIMESTAMP NOT NULL
    , create_dt         TIMESTAMP NOT NULL
    , update_dt         TIMESTAMP NOT NULL
);

COMMENT ON TABLE coupons IS '쿠폰 정책 테이블';
COMMENT ON COLUMN coupons.title IS '쿠폰명' ;
COMMENT ON COLUMN coupons.coupon_gubun IS '쿠폰 종류' ;
COMMENT ON COLUMN coupons.total_cnt IS '쿠폰 최대 발급 수량' ;
COMMENT ON COLUMN coupons.issue_cnt IS '발급된 쿠폰 수량' ;
COMMENT ON COLUMN coupons.discount_amt IS '할인 금액' ;
COMMENT ON COLUMN coupons.min_avail_amt IS '최소 사용 금액' ;
COMMENT ON COLUMN coupons.issue_st IS '발급 시작 일시' ;
COMMENT ON COLUMN coupons.issue_end IS '발급 종료 일시' ;
COMMENT ON COLUMN coupons.create_dt IS '생성일' ;
COMMENT ON COLUMN coupons.update_dt IS '수정일' ;


-- 발급된 쿠폰 내역 테이블
CREATE TABLE coupon_issues (
      id           BIGSERIAL PRIMARY KEY
    , coupon_id    BIGINT NOT NULL
    , user_id      BIGINT NOT NULL
    , issue_dt     TIMESTAMP NOT NULL
    , use_dt       TIMESTAMP NULL
    , creat_dt     TIMESTAMP NOT NULL
    , update_dt    TIMESTAMP NOT NULL
);

COMMENT ON TABLE coupon_issues IS '발급된 쿠폰 내역 테이블';
COMMENT ON COLUMN coupon_issues.coupon_id IS '쿠폰 ID' ;
COMMENT ON COLUMN coupon_issues.user_id IS '유저 ID' ;
COMMENT ON COLUMN coupon_issues.issue_dt IS '쿠폰 발급 일시' ;
COMMENT ON COLUMN coupon_issues.use_dt IS '쿠폰 사용 일시' ;
COMMENT ON COLUMN coupon_issues.creat_dt IS '생성일' ;
COMMENT ON COLUMN coupon_issues.update_dt IS '수정일' ;
