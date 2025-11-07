# 电子书存储目录

本目录用于存储 IntelliBook-Mall 项目的电子书文件和封面图片。

## 目录结构

```
ebook-storage/
├── covers/                 # 封面图片存储目录
│   ├── 1.jpg              # 书籍ID为1的封面
│   ├── 2.jpg              # 书籍ID为2的封面
│   └── ...
├── books/                  # 电子书文件存储目录
│   ├── pdf/               # PDF 格式电子书
│   │   ├── 1.pdf
│   │   ├── 2.pdf
│   │   └── ...
│   ├── epub/              # EPUB 格式电子书
│   │   ├── 4.epub
│   │   ├── 5.epub
│   │   └── ...
│   └── mobi/              # MOBI 格式电子书
│       ├── 6.mobi
│       └── ...
└── temp/                   # 临时文件目录
    └── uploads/           # 文件上传临时目录
```

## 文件命名规范

- **封面图片**: `{book_id}.jpg` 或 `{book_id}.png`
- **电子书文件**: `{book_id}.{format}`

示例：
- `covers/1.jpg` - ID为1的电子书封面
- `books/pdf/1.pdf` - ID为1的PDF格式电子书
- `books/epub/4.epub` - ID为4的EPUB格式电子书

## 准备测试文件

### 获取免费测试电子书

1. **Project Gutenberg**: https://www.gutenberg.org/
   - 提供超过70,000本免费经典文学作品
   - 支持多种格式（EPUB, PDF, MOBI等）

2. **Open Library**: https://openlibrary.org/
   - 提供大量免费电子书
   - 可以在线阅读或下载

3. **Internet Archive**: https://archive.org/
   - 公共领域书籍资源
   - 多种格式可选

### 创建简单测试文件

如果只是测试，可以：
1. 使用 Word 创建简单文档，导出为 PDF
2. 使用在线工具将文本转换为 EPUB
3. 使用任意图片作为封面（建议尺寸：400x600 像素）

## 注意事项

1. **版权问题**: 仅使用公共领域或有授权的电子书
2. **文件大小**: 单个文件建议不超过 100MB
3. **文件格式**: 
   - 封面支持：JPG, PNG
   - 电子书支持：PDF, EPUB, MOBI
4. **备份**: 定期备份重要文件
5. **Git 管理**: 
   - 此目录已添加到 .gitignore
   - 仅目录结构会被提交，实际文件不会上传到 Git

## 下一步操作

1. 准备 5-10 个测试电子书文件
2. 准备对应的封面图片
3. 按照命名规范放置文件
4. 执行数据库初始化脚本（test-data.sql）
5. 启动应用并测试文件访问
