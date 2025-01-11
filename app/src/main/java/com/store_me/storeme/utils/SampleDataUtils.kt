package com.store_me.storeme.utils

import com.naver.maps.geometry.LatLng
import com.store_me.storeme.data.BannerContent
import com.store_me.storeme.data.BannerData
import com.store_me.storeme.data.BannerDetailData
import com.store_me.storeme.data.ChatRoomWithStoreInfoData
import com.store_me.storeme.data.ContentType
import com.store_me.storeme.data.CouponWithStoreInfoData
import com.store_me.storeme.data.CustomLabelData
import com.store_me.storeme.data.DailyHoursData
import com.store_me.storeme.data.DetailCouponData
import com.store_me.storeme.data.LabelWithPostData
import com.store_me.storeme.data.LocationInfo
import com.store_me.storeme.data.MenuDatas
import com.store_me.storeme.data.NormalPostWithStoreInfoData
import com.store_me.storeme.data.MyPickWithStoreInfoData
import com.store_me.storeme.data.NearPlaceStoreWithStoreInfoData
import com.store_me.storeme.data.NotificationType
import com.store_me.storeme.data.NotificationWithStoreInfoData
import com.store_me.storeme.data.PreviewPostData
import com.store_me.storeme.data.SocialMediaAccountData
import com.store_me.storeme.data.StoreDetailData
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.StoreHomeItemData
import com.store_me.storeme.data.StoreHoursData
import com.store_me.storeme.data.StoreInfoData
import com.store_me.storeme.data.StoreMenuData
import com.store_me.storeme.data.StorePromotionData
import com.store_me.storeme.data.UserCouponWithStoreInfoData
import com.store_me.storeme.data.enums.PostType

class SampleDataUtils {

    companion object {
        private val sampleStoreInfo = mutableListOf(
            //CAFE
            StoreInfoData("0","YM ESPRESSO ROOM", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA0MTFfMTUg%2FMDAxNzEyODI1ODMzNzk2.D-Ust2ZplH6-pRnwEuxYXBc1rH6-HMv8dD6L_GwiPHQg.2H2D3iirFL6H3dhCr5Jt-xntRzX56hoGIZxqpuAtvc0g.JPEG%2F05077109-ACD6-498E-BE4B-6B5DA0DAC908.jpeg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.CAFE, "커피 판매", "진관동", 1138069000, 1500),
            StoreInfoData("1","HUGA", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240429_153%2F1714356738313qPQEl_PNG%2F%25C1%25A6%25B8%25F1%25C0%25BB_%25C0%25D4%25B7%25C2%25C7%25D8%25C1%25D6%25BC%25BC%25BF%25E4_-001_%25288%2529.png", "안녕하세요", StoreCategory.CAFE, "", "진관동", 1138069000, 1),
            StoreInfoData("2","필즈커피 FILLZ COFFEE", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDAxMjJfMjE4%2FMDAxNzA1OTE3MjA2OTcw.1e-UAeDm514kcN_zd5MXVQXQ-k2ZDT-CiCFrilSODTEg.PDJtKZgE311GnyatT2nFIzsOb3oikko1ukckM9rAEZYg.JPEG%2F20240122_164608.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.CAFE, "", "진관동", 1138069000, 1200),
            StoreInfoData("3","카페라또 구파발점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDAzMTBfMTMy%2FMDAxNzEwMDUxNDk1NDQ5.n-QGLetosnYtAp2Rktrv1eRi8sE_AfsbacgGs-02UHUg.kqI20qK0aSY1M55xJukPo0SraHs6g0zxzg75imMyyuYg.JPEG%2F20240310_150826.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.CAFE, "", "진관동", 1138069000, 1000),
            StoreInfoData("4","안스베이커리 롯데몰 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20161214_126%2F1481679867754z8cIM_JPEG%2F177159584641679_0.jpeg", "안녕하세요", StoreCategory.CAFE, "", "진관동", 1138069000, 1),
            StoreInfoData("5","외계인방앗간 은평뉴타운점", "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNDA3MDdfOTkg%2FMDAxNzIwMzA3MjQ4Njk3.gYW7gBe6ShbO9gsdwdMNq51UCT6KQzrVHwKSuPsjb6Eg.olSgs_rlLpGQVIkCNHi5FW9rolCXRHq8KRMTACrpAXog.JPEG%2F20240706%25A3%25DF144730.jpg", "안녕하세요", StoreCategory.CAFE, "", "진관동", 1138069000, 900),

            //RESTAURANT
            StoreInfoData("6","참나무본가 은평", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDAzMDJfMjk4%2FMDAxNzA5Mzc5OTIwOTgw.0SchI_vRfQYB0hIGv6ptD6UtVJ48EcJm0cB1fE1jdOYg.FUYgNxO0-OhHWfu5rlW5pS9siYIwwZMmyYG-np4ymT4g.JPEG%2F5FED871E-485B-4387-AEDF-95713253CC2F.jpeg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.RESTAURANT, "", "진관동", 1138069000, 800),
            StoreInfoData("7","탕면", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA2MTZfMzQg%2FMDAxNzE4NTEwMjcwMDAw.oI1f8qS_uKCowZCwcDwINvS9h4eiHjKF3Kw6eo38CB0g.xo0sugYC82T8qLDUX4Ox-3AEKlqt1QeNYCDVPhXJSmQg.JPEG%2F20240613_121750.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.RESTAURANT, "", "진관동", 1138069000, 400),
            StoreInfoData("8","에슐리퀸즈 롯데몰 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA3MTRfMjQ0%2FMDAxNzIwOTYzMjM5NzM0.QE72Im2gzZc9IfbN3DuA1RsFxbbv0BHjq81b59HA9jUg.M4FSBIHA18bMt1xCbvPME6tosNTeHPiVPtD75iRhCN4g.JPEG%2F20240714_192039.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.RESTAURANT, "", "진관동", 1138069000, 200),
            StoreInfoData("9","비와별닭갈비 롯데몰은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230519_127%2F1684482027520Pp9Co_JPEG%2F%25C0%25FC%25C3%25BC_01.jpg", "", StoreCategory.RESTAURANT, "", "진관동", 1138069000, 1),
            StoreInfoData("10","포옹싸", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDAyMTJfODgg%2FMDAxNzA3NzQ1MDgzNzU4.hlVs0KDav91MzBsqQ8lpLiMMKO1F4Qp4CO4qVNcooc8g.fQsVK-i1miXvcw61ZXRHcdaUAGegFZqtflZh1XR2LD4g.JPEG%2F8776C0A0-7253-4F89-8A76-0E14C3C6D7FD.jpeg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.RESTAURANT, "", "진관동", 1138069000, 80),
            StoreInfoData("11","스와가트 은평뉴타운점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240312_30%2F1710229317773Fjckq_JPEG%2FIMG_2957.jpeg", "안녕하세요", StoreCategory.RESTAURANT, "", "진관동", 1138069000, 1),

            //BEAUTY
            StoreInfoData("12","바디튠 구파발점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA1MjVfMjkw%2FMDAxNzE2NTY1MTQ3MzYx.xt9YFIng8lmcv8HG73xvBEBLDIT3Jw_ttiu_OU1OvYkg.9AxjToe30qtLUKFfiTW1wLPwGpuIsk3yhBVXQTDODQ0g.JPEG%2F20240522_125437.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.BEAUTY, "", "진관동", 1138069000, 800),
            StoreInfoData("13","멜로즈뷰티 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fnaverbooking-phinf.pstatic.net%2F20240301_249%2F1709274652362WhQxa_JPEG%2FKakaoTalk_20240301_013327061_04.jpg", "안녕하세요", StoreCategory.BEAUTY, "", "진관동", 1138069000, 500),
            StoreInfoData("14","세레니끄 롯데몰 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA2MjJfMjc5%2FMDAxNzE5MDQ3NjMzMDY2.KFWAEqF2TIujAJ-Z160pIgYp75JDYdi5QmGxVBrrTdog.pjT9grzPF5Itnqc17Iokh8l32sOxur85rpqW-xUSJZUg.JPEG%2F20240622_154757.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.BEAUTY, "", "진관동", 1138069000, 100),
            StoreInfoData("15","황제전통타이마사지", "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAxOTA0MjJfMjU5%2FMDAxNTU1ODkyOTMwMjIz.OOfANcWyBFdBT6DHunlkNKHlEmqKpq-8WZz6y-uM1KMg.G-wAXA143RgXxEiR71oNFy3MrjYXG75wWPg3ZhCC6Isg.JPEG.kjo105303%2FNaverBlog_20190422_092849_07.jpg", "안녕하세요", StoreCategory.BEAUTY, "", "진관동", 1138069000, 800),
            StoreInfoData("16","바른네일 롯데몰 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221201_286%2F1669888510937V5Mxy_JPEG%2FSNOW_20221201_185305_017.jpg", "안녕하세요", StoreCategory.BEAUTY, "", "진관동", 1138069000, 600),
            StoreInfoData("17","스파하라 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA2MTdfMjIy%2FMDAxNzE4NTk3MDY1MDg5.3xesJng6n_Wtp6lC3yJnxdVExw7Wg_tRDHYZlJchAwMg.YLZfu-KoIPPgSI1jXwWDwGeYXO8wiyqYf7FCJVrQzE4g.JPEG%2F20240617_115939.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.BEAUTY, "", "진관동", 1138069000, 545),

            //MEDICAL
            StoreInfoData("18","미소가인피부과의원 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA3MDNfMTI3%2FMDAxNzE5OTc5NDg5MzY5.UW489Ui_uK0wrW0Rr5WTdaSeb1zl6-b0ri1Od-PvrXkg.hHMjeT4OC55PBiPStPcNOXZhh1Q67e5eoOk9bKdZhzAg.JPEG%2F20240701_103642.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.MEDICAL, "", "진관동", 1138069000, 683),
            StoreInfoData("19","은평센트럴소아청소년과의원", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220928_93%2F1664337513202KV9hR_PNG%2F%25A1%25DA%25C0%25BA%25C6%25F2%25BC%25BE%25C6%25AE%25B7%25B2%25BC%25D2%25BE%25C6%25C3%25BB%25BC%25D2%25B3%25E2%25B0%25FA_%25BF%25A5%25BA%25ED%25B7%25B3.png", "안녕하세요", StoreCategory.MEDICAL, "", "진관동", 1138069000, 542),
            StoreInfoData("20","서울에스치과의원", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240611_143%2F17180915565468rjkq_JPEG%2FKakaoTalk_20240611_163741682.jpg", "안녕하세요", StoreCategory.MEDICAL, "", "진관동", 1138069000, 210),
            StoreInfoData("21","삼성레이디여성의원", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyMzEwMjhfMjk3%2FMDAxNjk4NDQ5MDI4NTI1.Wmo8jYtQfHPu8vqdXDvn3YCpqo2hAzVKaX50_tQqQh8g.6Blb3QYmaOVwekiZDWmdtJ6GsZEgnCvIB3E3_NFebvIg.JPEG%2Fupload_45521df53bf9d914e64deb82afba5e28.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.MEDICAL, "", "진관동", 1138069000, 340),
            StoreInfoData("22","유니스의원", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDAyMDJfMjQ0%2FMDAxNzA2ODc5MDg1NjEy.rdJUEO5_WRltv5i-srYotLeXxEOfWHM8fbzALfYNSaog.Dsdse_HYbRuYgpQDhNgkhx8oWEaNyHOROrJI-qn_L_cg.JPEG%2F20240202_105601.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.MEDICAL, "", "진관동", 1138069000, 85),
            StoreInfoData("23","성모튼튼신경외과의원", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240115_244%2F1705307841803lK5Wm_JPEG%2FKakaoTalk_20240115_173557604.jpg", "안녕하세요", StoreCategory.MEDICAL, "", "진관동", 1138069000, 63),

            //EXERCISE
            StoreInfoData("24","은평통일로스포츠센터", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA1MjdfMTM5%2FMDAxNzE2Nzg3NjkzNTAy.9tIjBE_9yGagKj5vZMoTMEl5yalcMr376fAv3BqKGEYg.RQh-q8aBdOcrjqtvEjWj-sUo_4WXjBZnbtP0n5PdfzYg.JPEG%2F1716710675621.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.EXERCISE, "", "진관동", 1138069000, 4),
            StoreInfoData("25","하이풀스포츠 x 온마인드 심리상담센터 롯데몰 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyMzExMTlfMjk4%2FMDAxNzAwMzg4Nzk5MzMw.7LkrH_tU6V8bfvA5GOnqhdSUDmGowOGpiof4fCKHYQcg.lLnVbkr4Ydimx5DyWBE-cvdIxiHAhfbVdOKrEBBPrGEg.JPEG%2F71232ADA-75B0-4521-82A6-6A672F9C4D33.jpeg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.EXERCISE, "", "진관동", 1138069000, 56),
            StoreInfoData("26","라우드짐&PT 구파발점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240710_116%2F1720614543529nrVS0_JPEG%2F%25C1%25A6%25B8%25F1%25C0%25BB_%25C0%25D4%25B7%25C2%25C7%25CF%25BC%25BC%25BF%25E4_%252840%2529.jpg", "안녕하세요", StoreCategory.EXERCISE, "", "진관동", 1138069000, 87),
            StoreInfoData("27","은평구민체육센터", "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzAyMjFfMTgz%2FMDAxNjc2OTQyMjIyODI5.v0BaWrJTsadKAh3vDTR0qixBZVv87yOYRXngJyvqHocg.jOE5sr7GGKIFwPl-zsXGddyKYP1NRsUhp8vbZzFTVo4g.PNG.eunpyeonggu%2F1%25C0%25CE_%25C5%25A9%25B8%25AE%25BF%25A1%25C0%25CC%25C5%25CD_%25BD%25E6%25B3%25D7%25C0%25CF_%25C5%25DB%25C7%25C3%25B8%25B4.png", "안녕하세요", StoreCategory.EXERCISE, "", "진관동", 1138069000, 86),
            StoreInfoData("28","로얄마린 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA2MjZfMTE4%2FMDAxNzE5MzgyODE1MTAz.q6JQHynfKYLC54JIF4dmhWLDFdUXUl4kynyzwqNlZPsg.c1eMPYsf8PoWdyaaXM7zIDDPk9JRVA7o406--aOrs7Ag.JPEG%2F20240619_143257.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.EXERCISE, "", "진관동", 1138069000, 33),
            StoreInfoData("29","가인볼링장 롯데몰 은평점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA2MjVfMTgx%2FMDAxNzE5Mjk2NjQ0ODk1.333v5ugsyFlbsiLfhg4CCeAO8GqVYxqAd3oOaEDNlpgg.vDQ8jG-pvTjk2asj9nIvZQoWUQW43pO7TDBvc40cdj0g.JPEG%2FB7E20F77-EDF6-4634-99B0-6459C417023D.jpeg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.EXERCISE, "", "진관동", 1138069000, 79),

            //SALON
            StoreInfoData("30","살롱드프롬헤어 구파발점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20190504_53%2F1556901001210dOhFi_JPEG%2FmfRXiuJAhMDfEvb46r_uDFn7.jpeg.jpg", "안녕하세요", StoreCategory.SALON, "", "진관동", 1138069000, 87),
            StoreInfoData("31","보그헤어 은평뉴타운점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDAzMjRfMTAz%2FMDAxNzExMjczMjY1MzI0.S1R4mURexzLs1meEuhi4B0FBB2cnOBrkq0-GbcdmIXYg.OAAigFGy_9n51-wm8r0yiguoUuPBEA_5Qpf1GJS5DIog.JPEG%2F20240324_180932.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.SALON, "", "진관동", 1138069000, 6),
            StoreInfoData("32","토리헤어 은평뉴타운점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA3MTJfNjQg%2FMDAxNzIwNzczMDUxMDEz.k7TLYT1NMK71LU6o0siYkboGDvM_7VB4vEjE3akNuk0g.jcye2vLkhRaotKQmk-SDI34Cj_dedLOqgPtq1cZn8OIg.JPEG%2F20240712_172922.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.SALON, "", "진관동", 1138069000, 667),
            StoreInfoData("33","아이디헤어 은평뉴타운점", "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNDA2MDVfMTEx%2FMDAxNzE3NTU3NTY1NDA3.1zG0Oav6t6RhbGgQRwRQT10n165xwQIJwHb3rFBBzqEg.UwYAsLOmKwO4IE8GKVzmwCph0gVmB0JCNiYkMX-uDNwg.JPEG%2FKakaoTalk_20240605_094834810.jpg", "안녕하세요", StoreCategory.SALON, "", "진관동", 1138069000, 86),
            StoreInfoData("34","본헤어 구파발역점", "https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA2MTZfMjk4%2FMDAxNzE4NDY0MDQ4MjIy.tZorHMTSwFHcHfg_1POJti10qAfkSfefvchWaOaEEwIg.qWrYaQntD6weGmkVGsUZTXFfvYT6I0DEbl4mid5YqiUg.JPEG%2F20240615_164611.jpg.jpg%3Ftype%3Dw1500_60_sharpen", "안녕하세요", StoreCategory.SALON, "", "진관동", 1138069000, 8676),
            StoreInfoData("35","준오헤어 은평롯데몰점", "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzExMDJfMTgz%2FMDAxNjk4ODg5MTc5MjMy.gShsFDVhR7qIMBCPkFKJV8FvuIftteFSsn2mGLr39Jcg.O8IP_hBrM_udtQ4rxWfBhwnUNupiUzem2YKmID4X1CYg.JPEG.jangli2%2FKakaoTalk_20231030_104851293.jpg", "안녕하세요", StoreCategory.SALON, "", "진관동", 1138069000, 863),

            )

        private val sampleStorePromotionData = mutableListOf(
            StorePromotionData("0", isCouponExist = true, isEventExist = true),
            StorePromotionData("1", isCouponExist = true, isEventExist = true),
            StorePromotionData("2", isCouponExist = true, isEventExist = false),
            StorePromotionData("3", isCouponExist = false, isEventExist = true),
            StorePromotionData("4", isCouponExist = true, isEventExist = true),
            StorePromotionData("5", isCouponExist = false, isEventExist = false),
            StorePromotionData("6", isCouponExist = false, isEventExist = false),
            StorePromotionData("7", isCouponExist = true, isEventExist = true),
            StorePromotionData("8", isCouponExist = true, isEventExist = true),
            StorePromotionData("9", isCouponExist = true, isEventExist = false),
            StorePromotionData("10", isCouponExist = false, isEventExist = true),
            StorePromotionData("11", isCouponExist = true, isEventExist = true),
            StorePromotionData("12", isCouponExist = false, isEventExist = false),
            StorePromotionData("13", isCouponExist = false, isEventExist = false),
            StorePromotionData("14", isCouponExist = true, isEventExist = true),
            StorePromotionData("15", isCouponExist = true, isEventExist = true),
            StorePromotionData("16", isCouponExist = true, isEventExist = false),
            StorePromotionData("17", isCouponExist = false, isEventExist = true),
            StorePromotionData("18", isCouponExist = true, isEventExist = true),
            StorePromotionData("19", isCouponExist = false, isEventExist = false),
            StorePromotionData("20", isCouponExist = false, isEventExist = false),
            StorePromotionData("21", isCouponExist = true, isEventExist = true),
            StorePromotionData("22", isCouponExist = true, isEventExist = false),
            StorePromotionData("23", isCouponExist = false, isEventExist = true),
            StorePromotionData("24", isCouponExist = true, isEventExist = true),
            StorePromotionData("25", isCouponExist = false, isEventExist = false),
            StorePromotionData("26", isCouponExist = false, isEventExist = false),
            StorePromotionData("27", isCouponExist = true, isEventExist = true),
            StorePromotionData("28", isCouponExist = true, isEventExist = true),
            StorePromotionData("29", isCouponExist = true, isEventExist = false),
            StorePromotionData("30", isCouponExist = false, isEventExist = true),
            StorePromotionData("31", isCouponExist = true, isEventExist = true),
            StorePromotionData("32", isCouponExist = false, isEventExist = false),
            StorePromotionData("33", isCouponExist = false, isEventExist = false),
            StorePromotionData("34", isCouponExist = false, isEventExist = false),
            StorePromotionData("35", isCouponExist = false, isEventExist = false),
        )

        val sampleDetailData = StoreDetailData(
            sampleStoreInfo[0],
            "https://via.placeholder.com/400x200",
            SocialMediaAccountData(listOf(
                "https://naver.me/FYImUXMi",
                "https://www.instagram.com/ymcoffeeproject?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
                "https://band.us/@byeonbanjangfruit",
                "https://youtube.com/@ytnnews24?si=kwF4N9T8q9VOK4JL",
                "https://www.google.com/",
            )),
            StoreHoursData(listOf(
                DailyHoursData(9, 0, 21, 0, 13, 0, 15, 0, false, false),
                DailyHoursData(9, 0, 21, 0, 13, 0, 15, 0, false, false),
                DailyHoursData(9, 0, 21, 0, 13, 0, 15, 0, false, false),
                DailyHoursData(9, 0, 21, 0, 13, 0, 15, 0, false, false),
                DailyHoursData(9, 0, 21, 0, 13, 0, 15, 0, false, false),
                DailyHoursData(9, 0, 21, 0, 13, 0, 15, 0, false, false),
                DailyHoursData(9, 0, 21, 0, 13, 0, 15, 0, false, false),
            ), temporaryOpeningHours = emptyList() ,closedDay = listOf()),
            storePhoneNumber = "070-7767-0829",
            locationInfo = LocationInfo("서울특별시 은평구 진관3로 43-9 래미안 909동 1층 101호", latLng = LatLng(37.6406673263033, 126.920399781789)),
            StoreMenuData(
                listOf(
                    MenuDatas("에스프레소", true, "원두 선택 : 시그니처 홈 블렌드 / 에티오피아 내추럴 / 디카페인 / 게이샤 워시드", 5800),
                    MenuDatas("아메리카노", true, "원두 선택 : 시그니처 홈 블렌드 / 에티오피아 내추럴 / 디카페인 / 게이샤 워시드", 5800),
                    MenuDatas("카페 라떼", false, "원두 선택 : 시그니처 홈 블렌드 / 에티오피아 내추럴 / 디카페인 / 게이샤 워시드", 6300),
                    MenuDatas("플랫 화이트", false, "원두 선택 : 시그니처 홈 블렌드 / 에티오피아 내추럴 / 디카페인 / 게이샤 워시드", 6300),
                    MenuDatas("카푸치노", false, "에스프레소와 폭신한 스팀밀크. 시나몬 파우더와 코코아 파우더. YM’s pick!", 6300),
                    MenuDatas("", false, "", 5000),
                    MenuDatas("", false, "", 5000),
                )
            ),
            CustomLabelData( listOf("로스팅 꿀팁", "일일 보고서")),
            "",
            listOf(
                LabelWithPostData("로스팅 꿀팁", listOf(PreviewPostData("https://via.placeholder.com/200x200", "1:1 이미지 예시입니다!", "2024-07-02T13:06:39"),
                    PreviewPostData("https://via.placeholder.com/400x200", "2:1 이미지 예시입니다!", "2024-07-02T13:06:39"),)),
                LabelWithPostData("일일 보고서", listOf(PreviewPostData("https://via.placeholder.com/400x300", "4:3 이미지 예시입니다!", "2024-07-02T13:06:39"))),
            ),
            isStoryExist = false,
            isReviewExist = false,
            couponList = emptyList(),
            representPhoto = emptyList()
        )

        val sampleCouponDetailData = listOf(
            DetailCouponData("COUPON_1", "전 제품 10% 할인 이용권", "2024-07-02T13:06:39", 30),
            DetailCouponData("COUPON_2", "전 제품 20% 할인 이용권", "2024-08-02T13:06:39", 50)
        )

        fun sampleBannerImage() : MutableList<BannerData>{
            return mutableListOf(
                BannerData("0","스토어미와", "https://via.placeholder.com/500x100"),
                BannerData("0","스토어미와 함께하는 ", "https://via.placeholder.com/750x150"),
                BannerData("0","스토어미와 함께하는 리뷰", "https://via.placeholder.com/1000x200"),
                BannerData("0","스토어미와 함께하는 리뷰 이벤트", "https://via.placeholder.com/1250x250"),
                BannerData("0","스토어미와 함께하는 리뷰 이벤트스토어미와 함께하는 리뷰 이벤트스토어미와 함께하는 리뷰 이벤트", "https://via.placeholder.com/1500x300"),
            )
        }

        fun sampleTodayStore(): MutableList<StoreInfoData>{
            return sampleStoreInfo
        }

        fun sampleCoupon(): MutableList<CouponWithStoreInfoData>{
            return mutableListOf(
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[0], "고기/김치만두 2개 증정"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[1], "2000원 할인 쿠폰"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[2], "쿠키 3개 증정"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[3], "치즈 크러스트 엣지 무료 교환권"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[4], "초코머핀 1개 무료 교환권"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[5], "치킨무 1개 무료 교환권"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[6], "PT 1회 이용권 + 1개월 할인권"),
            )
        }

        fun sampleUserCoupon(): MutableList<UserCouponWithStoreInfoData>{
            return mutableListOf(
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[0], "고기/김치만두 2개 증정", "2024-07-01T13:06:39", "2024-07-14T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[1], "2000원 할인 쿠폰", "2024-07-02T13:06:39", "2024-07-13T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[2], "쿠키 3개 증정", "2024-07-03T13:06:39", "2024-07-12T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[3], "치즈 크러스트 엣지 무료 교환권", "2024-07-04T13:06:39", "2024-07-11T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[4], "초코머핀 1개 무료 교환권", "2024-07-05T13:06:39", "2024-07-10T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[5], "치킨무 1개 무료 교환권", "2024-07-06T13:06:39", "2024-07-09T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[6], "PT 1회 이용권 + 1개월 할인권", "2024-07-07T13:06:39", "2024-07-08T13:06:39", false),
            )
        }

        fun sampleNotification(): MutableList<NotificationWithStoreInfoData>{
            return mutableListOf(
                NotificationWithStoreInfoData(NotificationType.NORMAL, "2024-07-10T13:06:39",sampleStoreInfo[0], ContentType.COUPON_RECEIVED, "마카롱 종류 300개 이용권",false),
                NotificationWithStoreInfoData(NotificationType.RESERVATION, "2024-07-11T13:06:39",sampleStoreInfo[1], ContentType.RESERVATION_DATE, "다데기",false),
                NotificationWithStoreInfoData(NotificationType.NORMAL, "2024-07-12T13:06:39",sampleStoreInfo[2], ContentType.COUPON_RECEIVED, null,false),
                NotificationWithStoreInfoData(NotificationType.NORMAL, "2024-07-13T13:06:39",sampleStoreInfo[3], ContentType.STORE_UPDATE, "이벤트 알림",false)
            )
        }

        fun sampleMyPick(): MutableList<MyPickWithStoreInfoData>{
            return mutableListOf(
                MyPickWithStoreInfoData(sampleStoreInfo[0], true),
                MyPickWithStoreInfoData(sampleStoreInfo[1], false),
                MyPickWithStoreInfoData(sampleStoreInfo[2], false),
                MyPickWithStoreInfoData(sampleStoreInfo[3], true),
                MyPickWithStoreInfoData(sampleStoreInfo[4], false),
                MyPickWithStoreInfoData(sampleStoreInfo[5], true),
                MyPickWithStoreInfoData(sampleStoreInfo[6], false),
            )
        }

        fun samplePost(): MutableList<NormalPostWithStoreInfoData>{
            return mutableListOf(
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0],"label", PostType.NORMAL, "2024-07-01T13:06:39" ,listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"),"1번 게시물 제목입니다.", "1번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0],"label", PostType.NORMAL, "2024-07-01T13:06:39", null,"2번 게시물 제목입니다.", "2번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0],"label", PostType.NORMAL, "2024-07-01T13:06:39", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"),"3번 게시물 제목입니다.", "3번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0],"label", PostType.NORMAL, "2024-07-01T13:06:39", null,"4번 게시물 제목입니다.", "4번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0],"label", PostType.NORMAL, "2024-07-01T13:06:39", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"),"5번 게시물 제목입니다.", "4번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0],"label", PostType.NORMAL, "2024-07-01T13:06:39", null,"6번 게시물 제목입니다.", "6번 게시물 내용입니다")
            )
        }

        fun sampleChatRooms(): MutableList<ChatRoomWithStoreInfoData>{
            return mutableListOf(
                ChatRoomWithStoreInfoData("1", "1번 채팅방", sampleStoreInfo[0], "안녕하세요", "17:30", 30),
                ChatRoomWithStoreInfoData("2", "2번 채팅방", sampleStoreInfo[1], "안녕하세요", "17:30", 30),
                ChatRoomWithStoreInfoData("3", "3번 채팅방", sampleStoreInfo[2], "안녕하세용", "9:50", 1),
                ChatRoomWithStoreInfoData("4", "4번 채팅방", sampleStoreInfo[3], "테스트 메시징", "1일 전", 99),
                ChatRoomWithStoreInfoData("5", "5번 채팅방", sampleStoreInfo[4], "넵! 내일 20시 예약 완료했습니다!", "1주 전", 1),
                ChatRoomWithStoreInfoData("6", "6번 채팅방", sampleStoreInfo[5], "모두 직접 가져오는 과일입니다.", "1달 전", 500),
                ChatRoomWithStoreInfoData("7", "7번 채팅방", sampleStoreInfo[6], "이번 만두는 바르지못해 폐기하였습니다.", "1년 전", 0),
            )
        }

        fun sampleNearPlaceStoreWithStoreInfoData(): List<NearPlaceStoreWithStoreInfoData>{
            return listOf(
                NearPlaceStoreWithStoreInfoData("\uD83C\uDF70 디저트가 맛있는 집", StoreCategory.CAFE,
                    listOf(sampleStoreInfo[0], sampleStoreInfo[1], sampleStoreInfo[2], sampleStoreInfo[3], sampleStoreInfo[4], sampleStoreInfo[5], ),
                    listOf(sampleStorePromotionData[0], sampleStorePromotionData[1], sampleStorePromotionData[2], sampleStorePromotionData[3], sampleStorePromotionData[4], sampleStorePromotionData[5]),
                ),
                NearPlaceStoreWithStoreInfoData("\uD83C\uDF74 맛있는 식당", StoreCategory.RESTAURANT,
                    listOf(sampleStoreInfo[6], sampleStoreInfo[7], sampleStoreInfo[8], sampleStoreInfo[9], sampleStoreInfo[10], sampleStoreInfo[11], ),
                    listOf(sampleStorePromotionData[6], sampleStorePromotionData[7], sampleStorePromotionData[8], sampleStorePromotionData[9], sampleStorePromotionData[10], sampleStorePromotionData[11]),
                ),
                NearPlaceStoreWithStoreInfoData("\uD83D\uDC84 아름다움을 위한 뷰티샵", StoreCategory.BEAUTY,
                    listOf(sampleStoreInfo[12], sampleStoreInfo[13], sampleStoreInfo[14], sampleStoreInfo[15], sampleStoreInfo[16], sampleStoreInfo[17], ),
                    listOf(sampleStorePromotionData[12], sampleStorePromotionData[13], sampleStorePromotionData[14], sampleStorePromotionData[15], sampleStorePromotionData[16], sampleStorePromotionData[17]),
                ),
                NearPlaceStoreWithStoreInfoData("\uD83C\uDFE5 가까운 의료기관", StoreCategory.MEDICAL,
                    listOf(sampleStoreInfo[18], sampleStoreInfo[19], sampleStoreInfo[20], sampleStoreInfo[21], sampleStoreInfo[22], sampleStoreInfo[23], ),
                    listOf(sampleStorePromotionData[18], sampleStorePromotionData[19], sampleStorePromotionData[20], sampleStorePromotionData[21], sampleStorePromotionData[22], sampleStorePromotionData[23]),
                ),
                NearPlaceStoreWithStoreInfoData("\uD83C\uDFB9 다양하게 즐기는 취미생활", StoreCategory.EXERCISE,
                    listOf(sampleStoreInfo[24], sampleStoreInfo[25], sampleStoreInfo[26], sampleStoreInfo[27], sampleStoreInfo[28], sampleStoreInfo[29], ),
                    listOf(sampleStorePromotionData[24], sampleStorePromotionData[25], sampleStorePromotionData[26], sampleStorePromotionData[27], sampleStorePromotionData[28], sampleStorePromotionData[29]),
                ),
                NearPlaceStoreWithStoreInfoData("\uD83D\uDC87 스타일 좋은 미용실", StoreCategory.SALON,
                    listOf(sampleStoreInfo[30], sampleStoreInfo[31], sampleStoreInfo[32], sampleStoreInfo[33], sampleStoreInfo[34], sampleStoreInfo[35], ),
                    listOf(sampleStorePromotionData[30], sampleStorePromotionData[31], sampleStorePromotionData[32], sampleStorePromotionData[33], sampleStorePromotionData[34], sampleStorePromotionData[35]),
                )
            )
        }

        fun sampleBannerDetailData(): BannerDetailData {
            return BannerDetailData("0", "\uD83C\uDF89 6월 스토어미 가입을 환영합니다!", "신규 사용자분들 위한 선물을 받아가세요.","2024-08-01T08:00:00", "2024-09-30T23:59:59", sampleBannerContent())

        }

        private fun sampleBannerContent(): List<BannerContent> {
            return listOf(
                BannerContent.TextContent("신규 사용자 여러분 스토어미 가입에 감사드립니다!\n" +
                        "오늘부터 스토어미와 ❤️+1일\n" +
                        "소중한 스토어미와의 만남을 기념하여 신규 사용자들을 위한 선물을 준비했어요.\n"),
                BannerContent.ImageContent("https://www.bccard.com/images/individual/popup/event/pop_2019090046_01.jpg"),
                BannerContent.TextContent("1. 스토어미 이벤트에 1회 이상 참여하고\n" +
                        "2. 스토어미에 기대하는 점을 댓글에 적어주세요!\n" +
                        "ex) 스토어미를 통해 원하는 가게 주문을 편하게 할 수 있어요.\n" +
                        "추첨을 통해 10분께 선물을 드립니다.\n" +
                        "· 발표일 : 2099년 7월 1일"),
                BannerContent.ImageContent("https://drdiary.hgodo.com/muhwadang/godo/join1.jpg")
            )
        }

        fun sampleStoreHomeItemData(): List<StoreHomeItemData> {
            return listOf(
                StoreHomeItemData(StoreHomeItem.NOTICE, false, 0),
                StoreHomeItemData(StoreHomeItem.INTRO, false, 1),
                StoreHomeItemData(StoreHomeItem.PHOTO, false, 2),
                StoreHomeItemData(StoreHomeItem.COUPON, false, 3),
                StoreHomeItemData(StoreHomeItem.MENU, false, 4),
                StoreHomeItemData(StoreHomeItem.STORY, false, 5),
                StoreHomeItemData(StoreHomeItem.REVIEW, false, 6),
                StoreHomeItemData(StoreHomeItem.NEWS, false, 7),
            )
        }
    }
}