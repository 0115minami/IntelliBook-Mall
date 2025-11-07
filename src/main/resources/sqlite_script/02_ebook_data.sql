-- ============================================
-- 电子书数据初始化脚本（修正版）
-- 符合 IntelliBook-Mall 表结构设计
-- 注意：需要先执行 03_minimal_required_data.sql 初始化分类数据
-- ============================================

-- 插入电子书数据（符合 tb_ebook 表结构）
-- 注意：category_id 需要对应已存在的分类
-- 注意：book_id 会自动生成，从1开始递增
INSERT INTO tb_ebook (book_title, author, isbn, publisher, publish_date, book_intro, 
                      category_id, cover_img, page_count, price, language, tags, rating, rating_count, status) VALUES
-- 1. 深度学习入门 (计算机 > 人工智能，category_id=17)
('深度学习入门:基于 Python 的理论与实现', '斋藤康毅', '9787115485588', '人民邮电出版社', '2018-07-01',
 '《深度学习入门：基于Python的理论与实现》是深度学习真正意义上的入门书，深入浅出地剖析了深度学习的核心理论。',
 17, 'covers/1.jpg', 336, 5900, 'zh-CN', 'Python,深度学习,机器学习,神经网络', 4.6, 890, 1),

-- 2. 红玫瑰与白玫瑰 (文学 > 现代文学，category_id=23)
('红玫瑰与白玫瑰', '张爱玲', '9787530218617', '北京十月文艺出版社', '2019-01-01',
 '《红玫瑰与白玫瑰》是张爱玲的经典作品之一，讲述了男人心中永恒的两难选择。',
 23, 'covers/2.jpg', 256, 2800, 'zh-CN', '张爱玲,现代文学,爱情小说', 4.5, 3420, 1),

-- 3. TOEFL iBT 备考指南 (教育 > 外语学习，category_id=30)
('TOEFL iBT Preparation Book: Test Prep for Reading, Listening, Speaking, & Writing', 'Test Prep Books', '9781628454246', 
 'Test Prep Books', '2017-04-17',
 'A comprehensive study guide for the TOEFL iBT exam, covering all four sections with practice tests and strategies.',
 30, 'covers/3.jpg', 412, 3900, 'en-US', 'TOEFL,English,Test Preparation,Study Guide', 4.2, 567, 1),

-- 4. 国富论 (经济管理 > 经济学，category_id=29)
('国富论', '亚当·斯密', '9787508036083', '华夏出版社', '2005-01-01',
 '现代经济学的开山之作，系统阐述了自由市场经济理论，被誉为经济学的"圣经"。',
 29, 'covers/4.jpg', 1200, 6800, 'zh-CN', '经济学,古典经济学,亚当斯密,市场经济', 4.7, 2150, 1),

-- 5. 心：稻盛和夫的一生嘱托 (经济管理 > 企业管理，category_id=25)
('心：稻盛和夫的一生嘱托', '稻盛和夫', '9787115536198', '人民邮电出版社', '2020-05-01',
 '"稻盛哲学"集大成之作，讲述企业经营和人生哲学的智慧。',
 25, 'covers/5.jpg', 288, 4900, 'zh-CN', '稻盛和夫,企业管理,经营哲学,人生智慧', 4.8, 1680, 1),

-- 6. 飞鸟集 (文学 > 文学经典，category_id=21)
('飞鸟集', '泰戈尔', '9787544710985', '译林出版社', '2010-06-01',
 '本书精选泰戈尔最负盛名的代表作，共收录泰戈尔的三部散文诗集：《飞鸟集》、《吉檀迦利》和《园丁集》。',
 21, 'covers/6.jpg', 320, 2400, 'zh-CN', '泰戈尔,诗歌,散文诗,印度文学', 4.9, 5670, 1),

-- 7. 中国野菜图鉴 (生活健康 > 美食烹饪，category_id=34)
('中国野菜图鉴 (中国之美自然生态图鉴)', '刘全儒', '9787537750264', '山西科学技术出版社', '2015-04-01',
 '《中国野菜图鉴》是"中国之美 自然生态图鉴"系列作品中的一部，详细介绍了中国常见野菜的识别和食用方法。',
 34, 'covers/7.jpg', 256, 5800, 'zh-CN', '野菜,植物图鉴,美食,自然', 4.4, 432, 1),

-- 8. 茹素之乐 (生活健康 > 美食烹饪，category_id=34)
('茹素之乐: 美味素食菜谱100例', '亚历克斯·巴拉克斯', NULL, '浙江出版联合集团', '2020-01-01',
 '100道令人垂涎欲滴的素食菜谱，为您的生活带去奇妙的转变。健康、美味、环保的素食生活方式指南。',
 34, 'covers/8.jpg', 180, 3800, 'zh-CN', '素食,菜谱,健康饮食,烹饪', 4.3, 289, 1);

-- 插入电子书文件数据（符合 tb_ebook_file 表结构）
-- 注意：file_path 使用数字命名，与实际文件对应
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
-- 书籍1 - EPUB
(1, 'EPUB', 'books/epub/1.epub', 12345600),

-- 书籍2 - EPUB
(2, 'EPUB', 'books/epub/2.epub', 3456700),

-- 书籍3 - 暂无文件（可后续添加）

-- 书籍4 - EPUB, MOBI
(4, 'EPUB', 'books/epub/4.epub', 18765400),
(4, 'MOBI', 'books/mobi/4.mobi', 21234500),

-- 书籍5 - EPUB, MOBI
(5, 'EPUB', 'books/epub/5.epub', 5678900),
(5, 'MOBI', 'books/mobi/5.mobi', 6789000),

-- 书籍6 - EPUB
(6, 'EPUB', 'books/epub/6.epub', 4567800),

-- 书籍7 - EPUB
(7, 'EPUB', 'books/epub/7.epub', 15678900),

-- 书籍8 - EPUB
(8, 'EPUB', 'books/epub/8.epub', 8765400);

-- 数据插入完成
SELECT '=== 电子书数据初始化完成 ===' AS info;
SELECT '电子书数量: ' || COUNT(*) AS info FROM tb_ebook;
SELECT '电子书文件数量: ' || COUNT(*) AS info FROM tb_ebook_file;
