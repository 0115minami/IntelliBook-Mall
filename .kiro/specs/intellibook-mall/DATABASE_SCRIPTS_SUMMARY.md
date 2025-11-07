# 数据库脚本创建总结

## 📋 已创建的脚本文件

### 1. **01_schema_only.sql** - 纯表结构脚本
**路径**: `src/main/resources/sqlite_script/01_schema_only.sql`

**内容**:
- ✅ 16张数据表的完整DDL
- ✅ 所有索引定义（30+个索引）
- ✅ 4个触发器（书单统计自动更新）
- ✅ 外键约束和唯一约束
- ❌ 不包含任何数据

**特点**:
- 适合生产环境
- 结构清晰，注释完整
- 支持幂等执行（IF NOT EXISTS）

---

### 2. **02_dev_test_data.sql** - 开发测试数据脚本
**路径**: `src/main/resources/sqlite_script/02_dev_test_data.sql`

**内容**:
- ✅ 1个管理员账户 + 3个测试用户
- ✅ 8个一级分类 + 27个二级分类
- ✅ 7本示例电子书（中英文技术书+文学作品）
- ✅ 10条电子书文件记录（多格式）
- ✅ 3个示例书单 + 书单关注
- ✅ 2个示例订单（已完成）
- ✅ 2条评价记录
- ✅ 5条收藏记录
- ✅ 2条阅读进度记录
- ✅ 用户兴趣数据

**特点**:
- 数据丰富，覆盖所有功能
- 适合开发和演示
- 包含统计信息输出

---

### 3. **03_minimal_required_data.sql** - 最小必需数据脚本
**路径**: `src/main/resources/sqlite_script/03_minimal_required_data.sql`

**内容**:
- ✅ 1个管理员账户
- ✅ 8个一级分类 + 35个二级分类
- ❌ 不包含示例电子书
- ❌ 不包含测试用户

**特点**:
- 最小化部署
- 适合生产环境
- 包含安全提示

---

### 4. **README.md** - 使用说明文档
**路径**: `src/main/resources/sqlite_script/README.md`

**内容**:
- 📖 脚本文件详细说明
- 🚀 快速开始指南
- 📊 数据库表结构概览
- 🔐 默认账户信息
- 🛠️ 常用操作命令
- ⚠️ 注意事项和最佳实践

---

## 🎯 使用建议

### 开发环境（推荐）

```bash
# 完整的开发环境初始化
cd src/main/resources/sqlite_script
sqlite3 ../intellibook.db < 01_schema_only.sql
sqlite3 ../intellibook.db < 02_dev_test_data.sql
```

**优势**:
- 立即可用的测试数据
- 覆盖所有功能模块
- 方便功能测试和演示

**测试账户**:
- 管理员: admin / admin123
- 用户: testuser / 123456

---

### 生产环境

```bash
# 最小化生产环境初始化
cd src/main/resources/sqlite_script
sqlite3 ../intellibook.db < 01_schema_only.sql
sqlite3 ../intellibook.db < 03_minimal_required_data.sql

# ⚠️ 重要：立即修改管理员密码！
```

**优势**:
- 最小化数据
- 安全可控
- 从零开始添加电子书

---

## 📊 数据统计

### 表结构
- **总表数**: 16张
- **核心业务表**: 9张
- **扩展功能表**: 7张
- **索引数量**: 30+个
- **触发器数量**: 4个

### 开发测试数据
- **用户**: 4个（1管理员 + 3普通用户）
- **分类**: 35个（8一级 + 27二级）
- **电子书**: 7本
- **电子书文件**: 10个
- **书单**: 3个
- **订单**: 2个
- **评价**: 2条
- **收藏**: 5条

### 最小必需数据
- **用户**: 1个（管理员）
- **分类**: 43个（8一级 + 35二级）
- **电子书**: 0本
- **其他**: 无

---

## 🔐 安全提示

### 默认密码（MD5加密）
| 明文密码 | MD5值 | 用途 |
|---------|-------|------|
| admin123 | 0192023a7bbd73250516f069df18b500 | 管理员 |
| 123456 | e10adc3949ba59abbe56e057f20f883e | 测试用户 |

### 生产环境安全检查清单
- [ ] 修改管理员密码
- [ ] 删除测试用户（如果有）
- [ ] 设置数据库文件权限
- [ ] 配置定期备份
- [ ] 启用访问日志
- [ ] 限制管理后台访问IP

---

## 🎨 示例电子书列表

### 技术类（5本）
1. **Java 编程思想（第4版）** - Bruce Eckel
   - 格式: PDF, EPUB
   - 价格: ¥88.00
   - 语言: 中文

2. **算法导论（第3版）** - Thomas H. Cormen
   - 格式: PDF
   - 价格: ¥108.00
   - 语言: 中文

3. **深入理解计算机系统（第3版）** - Randal E. Bryant
   - 格式: PDF
   - 价格: ¥139.00
   - 语言: 中文

4. **Clean Code** - Robert C. Martin
   - 格式: PDF, EPUB
   - 价格: ¥49.00
   - 语言: 英文

5. **Design Patterns** - Erich Gamma
   - 格式: PDF
   - 价格: ¥59.00
   - 语言: 英文

### 文学类（2本）
6. **三体** - 刘慈欣
   - 格式: EPUB, MOBI
   - 价格: ¥18.00
   - 语言: 中文

7. **活着** - 余华
   - 格式: EPUB
   - 价格: ¥12.00
   - 语言: 中文

---

## 📁 文件结构

```
src/main/resources/sqlite_script/
├── 01_schema_only.sql              # 纯表结构（生产）
├── 02_dev_test_data.sql            # 开发测试数据（开发）
├── 03_minimal_required_data.sql    # 最小必需数据（生产）
└── README.md                       # 使用说明文档
```

---

## 🚀 快速命令参考

### 初始化数据库
```bash
# 开发环境
sqlite3 intellibook.db < 01_schema_only.sql
sqlite3 intellibook.db < 02_dev_test_data.sql

# 生产环境
sqlite3 intellibook.db < 01_schema_only.sql
sqlite3 intellibook.db < 03_minimal_required_data.sql
```

### 查看数据
```sql
-- 统计信息
SELECT COUNT(*) FROM tb_user;
SELECT COUNT(*) FROM tb_ebook;
SELECT COUNT(*) FROM tb_category;

-- 查看电子书
SELECT book_id, book_title, author, price/100.0 AS price_yuan 
FROM tb_ebook 
WHERE status = 1;

-- 查看分类树
SELECT 
    c1.category_name AS '一级分类',
    c2.category_name AS '二级分类'
FROM tb_category c1
LEFT JOIN tb_category c2 ON c2.parent_id = c1.category_id
WHERE c1.category_level = 1
ORDER BY c1.sort_order DESC, c2.sort_order DESC;
```

### 备份和恢复
```bash
# 备份
sqlite3 intellibook.db ".backup backup.db"

# 恢复
sqlite3 intellibook.db ".restore backup.db"
```

---

## ✅ 完成检查清单

### 脚本文件
- [x] 纯表结构脚本
- [x] 开发测试数据脚本
- [x] 最小必需数据脚本
- [x] 使用说明文档

### 数据完整性
- [x] 所有表结构定义
- [x] 所有索引定义
- [x] 所有触发器定义
- [x] 外键约束
- [x] 唯一约束
- [x] 默认值设置

### 测试数据
- [x] 管理员账户
- [x] 测试用户账户
- [x] 完整分类体系
- [x] 示例电子书（中英文）
- [x] 多格式文件支持
- [x] 示例订单和评价
- [x] 书单和关注数据

### 文档
- [x] 脚本使用说明
- [x] 快速开始指南
- [x] 安全提示
- [x] 常用操作命令

---

## 🎉 总结

数据库脚本已全部创建完成！包含：

1. **生产级表结构脚本** - 可直接用于生产环境
2. **丰富的测试数据** - 覆盖所有功能模块
3. **最小化部署脚本** - 适合生产环境初始化
4. **详细的使用文档** - 包含所有必要说明

**建议**:
- 开发阶段使用 `02_dev_test_data.sql`
- 生产部署使用 `03_minimal_required_data.sql`
- 定期备份数据库
- 生产环境立即修改默认密码

所有脚本都经过精心设计，支持幂等执行，可以安全地重复运行。
