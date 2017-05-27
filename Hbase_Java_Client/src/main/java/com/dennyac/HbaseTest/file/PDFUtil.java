package com.dennyac.HbaseTest.file;


import com.itextpdf.text.*;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by shuyun on 2017/3/22.
 */
public class PDFUtil {

    /*
     * 1、简单的生成一个pdf文件
     */
    public static void createPDFOne(String fileName){
        //Step 1—Create a Document.
        Document document = new Document();
        try {
            //Step 2—Get a PdfWriter instance.
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            //Step 3—Open the Document.
            document.open();
            //Step 4—Add content.
            document.add(new Paragraph("Hello World！"));
            //Step 5—Close the Document.
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            // step 5
            document.close();
        }
    }

    /**
     * 2、生成包含页面大小,页面背景色,页边空白,Title,Author,Subject,Keywords的pdf文件
     */
    public static void createPDFTwo(String filename) {

        //页面大小
        Rectangle rect = new Rectangle(PageSize.A4);
        //页面背景色
        rect.setBackgroundColor(BaseColor.ORANGE);

        Document document = new Document(rect);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            //PDF版本
            writer.setPdfVersion(PdfWriter.VERSION_1_7);

            //文档属性
            document.addTitle("create pdf file");
            document.addAuthor("eff666");
            document.addSubject("This is the subject of the PDF file.");
            document.addKeywords("This is the keyword of the PDF file.");

            document.open();

            //文档正文内容
            document.add(new Paragraph("Hello World!"));
            document.add(new Paragraph(new Date().toString()));


            //添加Page
            document.newPage();
            writer.setPageEmpty(false);

            document.add(new Paragraph("Second page"));
            document.newPage();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    /**
     * 3、设置密码
     * @param fileName
     */
    public static void createPDFThree(String fileName){
        //页面大小
        Rectangle rect = new Rectangle(PageSize.A4);
        //页面背景色
        rect.setBackgroundColor(BaseColor.ORANGE);

        Document document = new Document(rect);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));

            // 设置密码为："World"
//            writer.setEncryption("Hello".getBytes(), "World".getBytes(),
//                PdfWriter.ALLOW_SCREENREADERS,
//                PdfWriter.STANDARD_ENCRYPTION_128);
            writer.setEncryption("Hello".getBytes(), "World".getBytes(), PdfWriter.ALLOW_COPY
                    | PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);


            document.open();
            document.add(new Paragraph("Hello World"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    /**
     * 4、插入Chunk, Phrase, Paragraph, List
     * @param fileName
     */
    public static void createPDFFour(String fileName){
        //页面大小
        Rectangle rect = new Rectangle(PageSize.A4);
        //页面背景色
        rect.setBackgroundColor(BaseColor.WHITE);
        Document document = new Document(rect);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            //Chunk对象: a String, a Font, and some attributes
            document.add(new Chunk("Java"));
            document.add(new Chunk(" "));
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE);
            Chunk id = new Chunk("99", font);
            id.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
            id.setTextRise(6);
            document.add(id);
            document.add(Chunk.NEWLINE);

            document.add(new Chunk("C++"));
            document.add(new Chunk(" "));
            Font font2 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE);
            Chunk id2 = new Chunk("99", font2);
            id2.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
            id2.setTextRise(6);
            id2.setUnderline(0.2f, -2f);
            document.add(id2);
            document.add(Chunk.NEWLINE);

            //Phrase对象: a List of Chunks with leading
            document.newPage();
            document.add(new Phrase("Second page"));

            Phrase director = new Phrase();
            Chunk name = new Chunk("Java");
            name.setUnderline(0.2f, -2f);
            director.add(name);
            director.add(new Chunk(","));
            director.add(new Chunk(" "));
            director.add(new Chunk("98"));
            director.setLeading(24);
            document.add(director);

            Phrase director2 = new Phrase();
            Chunk name2 = new Chunk("C++");
            name2.setUnderline(0.2f, -2f);
            director2.add(name2);
            director2.add(new Chunk(","));
            director2.add(new Chunk(" "));
            director2.add(new Chunk("98"));
            director2.setLeading(24);
            document.add(director2);

            //Paragraph对象: a Phrase with extra properties and a newline
            document.newPage();
            document.add(new Paragraph("Third page"));

            Paragraph info = new Paragraph();
            info.add(new Chunk("Java "));
            info.add(new Chunk("97"));
            info.add(Chunk.NEWLINE);
            info.add(new Phrase("C++ "));
            info.add(new Phrase("97"));
            document.add(info);

            //List对象: a sequence of Paragraphs called ListItem
            document.newPage();
            document.add(new Paragraph("Fourth page"));
            List list = new List(List.ORDERED);
            for (int i = 0; i < 10; i++) {
                ListItem item = new ListItem(String.format("%s: %d movies", "country" + (i + 1), (i + 1) * 100),
                        new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE));
                List movielist = new List(List.ORDERED, List.ALPHABETICAL);
                movielist.setLowercase(List.LOWERCASE);

                for (int j = 0; j < 2; j++) {
                    ListItem movieitem = new ListItem("Title" + (j + 1));
                    List directorlist = new List(List.UNORDERED);
                    for (int k = 0; k < 3; k++) {
                        directorlist.add(String.format("%s, %s", "Name1" + (k + 1), "Name2" + (k + 1)));
                    }
                    movieitem.add(directorlist);
                    movielist.add(movieitem);
                }
                item.add(movielist);
                list.add(item);
            }
            document.add(list);
        } catch (IOException e){
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    /**
     * 5、插入Anchor, Image, Chapter, Section
     * @param fileName
     */
    public static void createPDFFive(String fileName){
        //页面大小
        Rectangle rect = new Rectangle(PageSize.A4);
        //页面背景色
        rect.setBackgroundColor(BaseColor.WHITE);

        Document document = new Document(rect);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            //Anchor对象: internal and external links
            Paragraph country = new Paragraph();
            Anchor dest = new Anchor("Java", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE));
            dest.setName("Java");
            dest.setReference("http://www.java.com");//external
            country.add(dest);
            country.add(String.format(": %d people", 10000));
            document.add(country);

            document.newPage();
            Anchor toUS = new Anchor("Go to first page.", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE));
            toUS.setReference("#CN");//internal
            document.add(toUS);

            //Image对象
            document.newPage();
            Image img = Image.getInstance("F:\\pdf\\1483795948_7815.png");
            img.setAlignment(Image.LEFT | Image.TEXTWRAP);
            img.setBorder(Image.BOX);
            img.setBorderWidth(10);
            img.setBorderColor(BaseColor.WHITE);
            img.scaleToFit(1000, 72);//大小
            img.setRotationDegrees(-30);//旋转
            document.add(img);

            //Chapter, Section对象（目录）
            document.newPage();
            Paragraph title = new Paragraph("Title");
            Chapter chapter = new Chapter(title, 1);

            title = new Paragraph("Section A");
            Section section = chapter.addSection(title);
            section.setBookmarkTitle("bmk");
            section.setIndentation(30);
            section.setBookmarkOpen(false);
            section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);

            Section subsection = section.addSection(new Paragraph("Sub Section A"));
            subsection.setIndentationLeft(20);
            subsection.setNumberDepth(1);

            document.add(chapter);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    /**
     * 6、位置调整
     * @param fileName
     */
    public static void createPDFSix(String fileName){
        //页面大小
        Rectangle rect = new Rectangle(PageSize.A4);
        //页面背景色
        rect.setBackgroundColor(BaseColor.WHITE);

        Document document = new Document(rect);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Paragraph p = new Paragraph("iText是一个非常著名的能够快速产生PDF文件的Java类库。支持文本，表格，图形的操作，可以方便的跟Servlet进行结合。iText不仅可以生成PDF或rtf的文档，而且可以将XML、Html文件转化为PDF文件。");
            //默认
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(p);

            document.newPage();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.setIndentationLeft(1 * 15f);
            p.setIndentationRight((5 - 1) * 15f);
            document.add(p);

            //居右
            document.newPage();
            p.setAlignment(Element.ALIGN_RIGHT);
            p.setSpacingAfter(15f);
            document.add(p);

            //居左
            document.newPage();
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingBefore(15f);
            document.add(p);

            //居中
            document.newPage();
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(15f);
            p.setSpacingBefore(15f);
            document.add(p);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }


    /**
     * 7、压缩PDF到Zip
     * @param fileName
     */
    public static void createPDFSeven(String fileName){
        //页面大小
       // Document document = new Document();
        ZipOutputStream zip = null;
        try {
            zip = new ZipOutputStream(new FileOutputStream(fileName));
            for (int i = 1; i <= 3; i++) {
                ZipEntry entry = new ZipEntry("pdf_00" + i + ".pdf");
                zip.putNextEntry(entry);
                Document document = new Document();
                PdfWriter writer = PdfWriter.getInstance(document, zip);
                writer.setCloseStream(false);
                document.open();
                document.add(new Paragraph("Hello " + i));
                document.close();
                zip.closeEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            try {
                zip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 8、HTML 转为 PDF
     * @param fileName
     */
    public static void createPDFEight(String fileName){
        Document document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader("<h1>This is a test!<body>\ntest</body></h1>"));
            document.close();

            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            HTMLWorker htmlWorker1 = new HTMLWorker(document);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }


    /**
     * 9、读取pdf文件内容
     * @param path
     */
    private static void readPDF(String path)
    {
        //byte[] contentBytes;
        //String byteToStr;
        String content = "";
        int pageNum = 0;
        try {
            PdfReader reader = new PdfReader(path);

            pageNum = reader.getNumberOfPages();//获得页数
            for (int i = 1; i < pageNum; i++) {// 从第1页开始读
                //contentBytes = reader.getPageContent(i);
                //byteToStr = new String(contentBytes, "UTF-8");// 转码有问题
                content += PdfTextExtractor.getTextFromPage(reader, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(content);
    }



}
