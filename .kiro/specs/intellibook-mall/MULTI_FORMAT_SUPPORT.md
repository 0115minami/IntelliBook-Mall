# 电子书多格式支持设计文档

## 更新概述

本次更新实现了电子书多格式支持功能，允许一本电子书拥有多种文件格式（PDF、EPUB、MOBI、AZW3等），用户购买后可以选择下载任意格式。

## 设计方案

### 方案选择

采用**独立文件表方案**（方案一），创建 `tb_ebook_file` 表来存储电子书的多种格式文件。

**优点**：
- ✅ 数据结构清晰，符合数据库范式
- ✅ 易于查询和管理
- ✅ 支持独立的下载统计
- ✅ 易于扩展新格式
- ✅ 适合生产环境

## 数据库设计

### 表结构变更

#### 1. tb_ebook 表（修改）

**变更内容**：
- ❌ 删除 `file_format` 字段
- ❌ 删除 `file_path` 字段  
- ❌ 删除 `file_size` 字段
- ✅ 保留其他所有元数据字段

**修改后的表结构**：
```sql
CREATE TABLE IF NOT EXISTS tb_ebook (
    book_id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20),
    publisher VARCHAR(100),
    publish_date DATE,
    book_intro TEXT,
    category_id INTEGER NOT NULL,
    cover_img VARCHAR(200),
    page_count INTEGER DEFAULT 0,
    price INTEGER DEFAULT 0,  -- 价格（分）
    language VARCHAR(10) DEFAULT 'zh-CN',
    rating REAL DEFAULT 0.0,
    rating_count INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,
    download_count INTEGER DEFAULT 0,  -- 所有格式的总下载次数
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES tb_category(category_id)
);
```

#### 2. tb_ebook_file 表（新增）

**新增表结构**：
```sql
CREATE TABLE IF NOT EXISTS tb_ebook_file (
    file_id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    file_format VARCHAR(10) NOT NULL,  -- PDF/EPUB/MOBI/AZW3等
    file_path VARCHAR(500) NOT NULL,
    file_size INTEGER,  -- 文件大小（字节）
    download_count INTEGER DEFAULT 0,  -- 该格式的下载次数
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id) ON DELETE CASCADE,
    UNIQUE(book_id, file_format)  -- 同一本书的同一格式只能有一个文件
);

CREATE INDEX idx_ebook_file_book ON tb_ebook_file(book_id);
CREATE INDEX idx_ebook_file_format ON tb_ebook_file(file_format);
```

**字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| file_id | INTEGER | 主键，自增ID |
| book_id | INTEGER | 书籍ID，外键关联 tb_ebook |
| file_format | VARCHAR(10) | 文件格式（PDF/EPUB/MOBI/AZW3等） |
| file_path | VARCHAR(500) | 文件存储路径 |
| file_size | INTEGER | 文件大小（字节） |
| download_count | INTEGER | 该格式的下载次数 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

**约束说明**：
- **唯一约束**：(book_id, file_format) 确保同一本书的同一格式只有一个文件
- **外键约束**：ON DELETE CASCADE 确保删除书籍时自动删除所有文件记录
- **索引优化**：book_id 和 file_format 索引提升查询性能

### 数据示例

```sql
-- 一本书的元数据
INSERT INTO tb_ebook (book_title, author, isbn, category_id, price) 
VALUES ('Java 编程思想', 'Bruce Eckel', '9787111213826', 6, 8800);

-- 同一本书的多种格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(1, 'PDF', 'books/pdf/1.pdf', 52428800),      -- 50 MB
(1, 'EPUB', 'books/epub/1.epub', 15728640),   -- 15 MB
(1, 'MOBI', 'books/mobi/1.mobi', 18874368);   -- 18 MB
```

## 实体设计

### Java 实体类

#### EBook.java
```java
@Data
public class EBook {
    private Long bookId;
    private String bookTitle;
    private String author;
    private String isbn;
    private String publisher;
    private Date publishDate;
    private String bookIntro;
    private Long categoryId;
    private String coverImg;
    private Integer pageCount;
    private Integer price;
    private String language;
    private Double rating;
    private Integer ratingCount;
    private Integer viewCount;
    private Integer downloadCount;  // 所有格式的总下载次数
    private Byte status;
    private Date createTime;
    private Date updateTime;
    
    // 关联属性（非数据库字段）
    @Transient
    private List<EBookFile> files;  // 该书的所有格式文件
}
```

#### EBookFile.java（新增）
```java
@Data
public class EBookFile {
    private Long fileId;
    private Long bookId;
    private String fileFormat;      // PDF/EPUB/MOBI/AZW3
    private String filePath;
    private Long fileSize;
    private Integer downloadCount;  // 该格式的下载次数
    private Date createTime;
    private Date updateTime;
}
```

## 服务层设计

### EBookFileService（新增）

```java
public interface EBookFileService {
    // 文件管理
    String uploadEBookFile(MultipartFile file, Long bookId, String format);
    Boolean deleteEBookFile(Long fileId);
    Boolean deleteEBookFileByFormat(Long bookId, String format);
    List<EBookFile> getEBookFiles(Long bookId);
    EBookFile getEBookFile(Long bookId, String format);
    
    // 文件验证
    Boolean validateFileFormat(String format);
    Boolean checkFileExists(Long bookId, String format);
    
    // 下载统计
    Boolean incrementDownloadCount(Long fileId);
    Integer getTotalDownloadCount(Long bookId);
    Map<String, Integer> getDownloadCountByFormat(Long bookId);
}
```

### EBookService（修改）

```java
public interface EBookService {
    // 获取电子书时自动加载所有格式文件
    EBook getEBookById(Long id);  // 返回包含 files 列表的完整信息
    
    // 其他方法保持不变
    PageResult<EBook> getEBookPage(PageQueryUtil pageUtil);
    String saveEBook(EBook ebook);
    String updateEBook(EBook ebook);
    // ...
}
```

## API 接口设计

### 1. 获取电子书详情（包含所有格式）

**请求**：
```http
GET /api/v1/ebooks/1
```

**响应**：
```json
{
  "resultCode": 200,
  "message": "success",
  "data": {
    "bookId": 1,
    "bookTitle": "Java 编程思想",
    "author": "Bruce Eckel",
    "isbn": "9787111213826",
    "price": 8800,
    "rating": 4.8,
    "files": [
      {
        "fileId": 1,
        "fileFormat": "PDF",
        "filePath": "books/pdf/1.pdf",
        "fileSize": 52428800,
        "downloadCount": 1250
      },
      {
        "fileId": 2,
        "fileFormat": "EPUB",
        "filePath": "books/epub/1.epub",
        "fileSize": 15728640,
        "downloadCount": 856
      },
      {
        "fileId": 3,
        "fileFormat": "MOBI",
        "filePath": "books/mobi/1.mobi",
        "fileSize": 18874368,
        "downloadCount": 432
      }
    ]
  }
}
```

### 2. 获取电子书的所有格式

**请求**：
```http
GET /api/v1/ebooks/1/files
```

**响应**：
```json
{
  "resultCode": 200,
  "message": "success",
  "data": [
    {
      "fileId": 1,
      "fileFormat": "PDF",
      "fileSize": 52428800,
      "downloadCount": 1250
    },
    {
      "fileId": 2,
      "fileFormat": "EPUB",
      "fileSize": 15728640,
      "downloadCount": 856
    },
    {
      "fileId": 3,
      "fileFormat": "MOBI",
      "fileSize": 18874368,
      "downloadCount": 432
    }
  ]
}
```

### 3. 上传电子书文件（管理员）

**请求**：
```http
POST /api/v1/admin/ebooks/1/files
Content-Type: multipart/form-data

file: [binary data]
format: PDF
```

**响应**：
```json
{
  "resultCode": 200,
  "message": "文件上传成功",
  "data": {
    "fileId": 4,
    "fileFormat": "PDF",
    "filePath": "books/pdf/1.pdf",
    "fileSize": 52428800
  }
}
```

### 4. 删除电子书文件（管理员）

**请求**：
```http
DELETE /api/v1/admin/ebooks/files/4
```

**响应**：
```json
{
  "resultCode": 200,
  "message": "文件删除成功"
}
```

### 5. 下载电子书（指定格式）

**请求**：
```http
POST /api/v1/reading/1/download
Content-Type: application/json

{
  "format": "PDF"
}
```

**响应**：
```json
{
  "resultCode": 200,
  "message": "success",
  "data": {
    "downloadUrl": "/api/v1/download?fileId=1&userId=123&timestamp=1234567890&sign=abc123",
    "expireTime": "2024-01-02T12:00:00",
    "fileFormat": "PDF",
    "fileSize": 52428800
  }
}
```

## Mapper 设计

### EBookFileMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.intellibook.mall.dao.EBookFileMapper">
    
    <!-- 查询某本书的所有格式文件 -->
    <select id="selectByBookId" resultType="EBookFile">
        SELECT * FROM tb_ebook_file
        WHERE book_id = #{bookId}
        ORDER BY file_format
    </select>
    
    <!-- 查询指定格式的文件 -->
    <select id="selectByBookIdAndFormat" resultType="EBookFile">
        SELECT * FROM tb_ebook_file
        WHERE book_id = #{bookId} AND file_format = #{format}
    </select>
    
    <!-- 插入文件记录 -->
    <insert id="insert" useGeneratedKeys="true" keyProperty="fileId">
        INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size)
        VALUES (#{bookId}, #{fileFormat}, #{filePath}, #{fileSize})
    </insert>
    
    <!-- 删除文件记录 -->
    <delete id="deleteByPrimaryKey">
        DELETE FROM tb_ebook_file WHERE file_id = #{fileId}
    </delete>
    
    <!-- 更新下载次数 -->
    <update id="incrementDownloadCount">
        UPDATE tb_ebook_file 
        SET download_count = download_count + 1,
            update_time = CURRENT_TIMESTAMP
        WHERE file_id = #{fileId}
    </update>
    
    <!-- 获取总下载次数 -->
    <select id="getTotalDownloadCount" resultType="Integer">
        SELECT COALESCE(SUM(download_count), 0)
        FROM tb_ebook_file
        WHERE book_id = #{bookId}
    </select>
    
</mapper>
```

## 业务逻辑实现

### 文件上传流程

```java
@Service
public class EBookFileServiceImpl implements EBookFileService {
    
    @Autowired
    private EBookFileMapper eBookFileMapper;
    
    @Override
    @Transactional
    public String uploadEBookFile(MultipartFile file, Long bookId, String format) {
        // 1. 验证文件格式
        if (!validateFileFormat(format)) {
            throw new IntelliBookException("不支持的文件格式");
        }
        
        // 2. 检查是否已存在该格式
        if (checkFileExists(bookId, format)) {
            throw new IntelliBookException("该格式文件已存在，请先删除");
        }
        
        // 3. 保存文件到磁盘
        String fileName = bookId + "." + format.toLowerCase();
        String filePath = "books/" + format.toLowerCase() + "/" + fileName;
        File dest = new File(uploadPath + filePath);
        file.transferTo(dest);
        
        // 4. 保存文件记录到数据库
        EBookFile eBookFile = new EBookFile();
        eBookFile.setBookId(bookId);
        eBookFile.setFileFormat(format);
        eBookFile.setFilePath(filePath);
        eBookFile.setFileSize(file.getSize());
        
        if (eBookFileMapper.insert(eBookFile) > 0) {
            return "文件上传成功";
        }
        throw new IntelliBookException("文件上传失败");
    }
    
    @Override
    public Boolean validateFileFormat(String format) {
        List<String> supportedFormats = Arrays.asList("PDF", "EPUB", "MOBI", "AZW3", "TXT");
        return supportedFormats.contains(format.toUpperCase());
    }
    
    @Override
    public Boolean checkFileExists(Long bookId, String format) {
        EBookFile file = eBookFileMapper.selectByBookIdAndFormat(bookId, format);
        return file != null;
    }
}
```

### 下载流程（支持格式选择）

```java
@Service
public class ReadingServiceImpl implements ReadingService {
    
    @Autowired
    private EBookFileService eBookFileService;
    
    @Override
    public String generateDownloadUrl(Long userId, Long bookId, String format) {
        // 1. 验证购买权限
        if (!verifyDownloadPermission(userId, bookId)) {
            throw new IntelliBookException("无下载权限");
        }
        
        // 2. 获取指定格式的文件
        EBookFile file = eBookFileService.getEBookFile(bookId, format);
        if (file == null) {
            throw new IntelliBookException("该格式文件不存在");
        }
        
        // 3. 生成临时下载链接
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = MD5Util.MD5Encode(
            userId + file.getFileId() + timestamp + SECRET_KEY, "UTF-8");
        
        String downloadUrl = String.format(
            "/api/v1/download?fileId=%d&userId=%d&timestamp=%s&sign=%s",
            file.getFileId(), userId, timestamp, signature);
        
        // 4. 增加下载次数
        eBookFileService.incrementDownloadCount(file.getFileId());
        
        return downloadUrl;
    }
}
```

## 前端展示

### 电子书详情页 - 格式选择

```html
<div class="ebook-formats">
  <h3>可用格式</h3>
  <div class="format-list">
    <div class="format-item" v-for="file in ebook.files" :key="file.fileId">
      <span class="format-name">{{ file.fileFormat }}</span>
      <span class="file-size">{{ formatFileSize(file.fileSize) }}</span>
      <span class="download-count">下载: {{ file.downloadCount }}</span>
      <button @click="downloadFile(file.fileFormat)">下载</button>
    </div>
  </div>
</div>
```

### 下载对话框

```javascript
async downloadFile(format) {
  try {
    const response = await axios.post(`/api/v1/reading/${this.bookId}/download`, {
      format: format
    });
    
    if (response.data.resultCode === 200) {
      // 打开下载链接
      window.location.href = response.data.data.downloadUrl;
      this.$message.success(`正在下载 ${format} 格式`);
    }
  } catch (error) {
    this.$message.error('下载失败');
  }
}
```

## 数据迁移

### 从旧表结构迁移

如果已有数据使用旧表结构，需要进行数据迁移：

```sql
-- 1. 创建新的文件表
CREATE TABLE IF NOT EXISTS tb_ebook_file (
    -- ... 表结构如上
);

-- 2. 迁移现有数据
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size)
SELECT book_id, file_format, file_path, file_size
FROM tb_ebook
WHERE file_format IS NOT NULL AND file_path IS NOT NULL;

-- 3. 删除旧字段（可选，建议先备份）
-- ALTER TABLE tb_ebook DROP COLUMN file_format;
-- ALTER TABLE tb_ebook DROP COLUMN file_path;
-- ALTER TABLE tb_ebook DROP COLUMN file_size;
```

## 优势总结

### 1. 用户体验提升
- ✅ 用户可以选择最适合自己设备的格式
- ✅ 一次购买，多种格式任意下载
- ✅ 不同设备使用不同格式（手机用EPUB，电脑用PDF）

### 2. 数据管理优化
- ✅ 数据结构清晰，符合数据库范式
- ✅ 易于查询和统计（如：哪种格式最受欢迎）
- ✅ 独立的下载统计，便于分析用户偏好

### 3. 系统扩展性
- ✅ 轻松添加新格式支持（如 AZW3、TXT）
- ✅ 可以为不同格式设置不同价格（未来扩展）
- ✅ 支持格式转换功能（未来扩展）

### 4. 毕业设计亮点
- ✅ 展示良好的数据库设计能力
- ✅ 体现对用户需求的深入理解
- ✅ 增加项目的技术含量和实用性

## 测试用例

### 1. 功能测试

```java
@Test
void testUploadMultipleFormats() {
    // 上传同一本书的多种格式
    eBookFileService.uploadEBookFile(pdfFile, 1L, "PDF");
    eBookFileService.uploadEBookFile(epubFile, 1L, "EPUB");
    eBookFileService.uploadEBookFile(mobiFile, 1L, "MOBI");
    
    // 验证文件数量
    List<EBookFile> files = eBookFileService.getEBookFiles(1L);
    assertEquals(3, files.size());
}

@Test
void testDownloadSpecificFormat() {
    // 下载指定格式
    String url = readingService.generateDownloadUrl(1L, 1L, "PDF");
    assertNotNull(url);
    assertTrue(url.contains("fileId="));
}

@Test
void testDuplicateFormatUpload() {
    // 测试重复上传同一格式
    eBookFileService.uploadEBookFile(pdfFile, 1L, "PDF");
    
    assertThrows(IntelliBookException.class, () -> {
        eBookFileService.uploadEBookFile(pdfFile2, 1L, "PDF");
    });
}
```

## 总结

通过引入 `tb_ebook_file` 表，成功实现了电子书多格式支持功能。这个设计：
- 符合数据库设计规范
- 提升了用户体验
- 增强了系统的扩展性
- 为毕业设计增加了技术亮点

该功能的实现展示了对实际业务需求的深入理解和良好的系统设计能力。
