-- ============================================
-- IntelliBook-Mall 最小必需数据脚本
-- 用途：生产环境最小化初始数据
-- 说明：仅包含系统运行必需的基础数据
-- ============================================

-- ============================================
-- 1. 管理员账户（必需）
-- ============================================

-- 默认管理员账户
-- 用户名: admin
-- 密码: admin123 (MD5: 0192023a7bbd73250516f069df18b500)
-- 建议：生产环境部署后立即修改密码
INSERT INTO tb_user (username, password, nickname, email, is_admin) VALUES 
('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', 'admin@intellibook.com', 1);

-- ============================================
-- 2. 电子书分类（必需）
-- ============================================

-- 一级分类（核心分类）
INSERT INTO tb_category (category_id, category_name, category_name_en, parent_id, category_level, sort_order) VALUES
(1, '计算机与互联网', 'Computer & Internet', 0, 1, 100),
(2, '文学小说', 'Literature & Fiction', 0, 1, 99),
(3, '经济管理', 'Business & Economics', 0, 1, 98),
(4, '教育学习', 'Education & Learning', 0, 1, 97),
(5, '生活健康', 'Health & Lifestyle', 0, 1, 96),
(6, '历史人文', 'History & Humanities', 0, 1, 95),
(7, '科学技术', 'Science & Technology', 0, 1, 94),
(8, '艺术设计', 'Arts & Design', 0, 1, 93);

-- 二级分类（计算机类）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('Java', 'Java', 1, 2, 100),
('Python', 'Python', 1, 2, 99),
('JavaScript', 'JavaScript', 1, 2, 98),
('前端开发', 'Frontend Development', 1, 2, 97),
('后端开发', 'Backend Development', 1, 2, 96),
('移动开发', 'Mobile Development', 1, 2, 95),
('算法与数据结构', 'Algorithms & Data Structures', 1, 2, 94),
('数据库', 'Database', 1, 2, 93),
('人工智能', 'Artificial Intelligence', 1, 2, 92),
('网络安全', 'Cybersecurity', 1, 2, 91);

-- 二级分类（文学类）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('科幻小说', 'Science Fiction', 2, 2, 100),
('悬疑推理', 'Mystery & Thriller', 2, 2, 99),
('都市言情', 'Romance', 2, 2, 98),
('文学经典', 'Literary Classics', 2, 2, 97),
('现代文学', 'Modern Literature', 2, 2, 96),
('历史小说', 'Historical Fiction', 2, 2, 95);

-- 二级分类（经济管理类）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('企业管理', 'Business Management', 3, 2, 100),
('市场营销', 'Marketing', 3, 2, 99),
('金融投资', 'Finance & Investment', 3, 2, 98),
('创业指南', 'Entrepreneurship', 3, 2, 97),
('经济学', 'Economics', 3, 2, 96);

-- 二级分类（教育学习类）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('考试辅导', 'Test Preparation', 4, 2, 100),
('外语学习', 'Language Learning', 4, 2, 99),
('职业技能', 'Professional Skills', 4, 2, 98),
('自我提升', 'Self-Improvement', 4, 2, 97);

-- 二级分类（生活健康类）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('健康养生', 'Health & Wellness', 5, 2, 100),
('美食烹饪', 'Cooking', 5, 2, 99),
('旅游地理', 'Travel & Geography', 5, 2, 98),
('家居生活', 'Home & Living', 5, 2, 97);

-- ============================================
-- 初始化完成提示
-- ============================================

SELECT '=== 最小必需数据初始化完成 ===' AS info;
SELECT '管理员账户: admin / admin123' AS info;
SELECT '一级分类数量: ' || COUNT(*) AS info FROM tb_category WHERE category_level = 1;
SELECT '二级分类数量: ' || COUNT(*) AS info FROM tb_category WHERE category_level = 2;
SELECT '总分类数量: ' || COUNT(*) AS info FROM tb_category;
SELECT '⚠️ 警告：请在生产环境中立即修改管理员密码！' AS info;
