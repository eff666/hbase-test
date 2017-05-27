package com.dennyac.HbaseTest.file;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * Created by shuyun on 2017/3/30.
 */
public class SaxParserXmlCode extends DefaultHandler {
    private int bookIndex = 0;
    private String currentTag = null;

    // 用来标示解析开始
    @Override
    public void startDocument() throws SAXException {
        System.out.println("SAX解析开始...");
    }

    // 用来标示解析结束
    @Override
    public void endDocument() throws SAXException {
        System.out.println("SAX解析结束...");
    }

    // 用来遍历XML文件的开始标签
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // 调用DefaultHandler类的startElement方法
        super.startElement(uri, localName, qName, attributes);
        currentTag = qName;
        // 开始解析book元素节点
        if(qName.equals("book")){
            ++ bookIndex;
            System.out.println("开始解析第" + bookIndex + "个节点...");
            // 已知book元素节点下的属性名称，根据属性名称获取属性值
			/*String value = attributes.getValue("category");
			System.out.println("value->"+value);*/

            // 不知道book元素节点下的属性名称以及个数
            int size = attributes.getLength();
            for(int i = 0;i < size; ++i){
                System.out.println(attributes.getQName(i) + "：" + attributes.getValue(i));
            }
        }else if(!qName.equals("bookstore")){
            System.out.print(qName + "：");
        }
    }

    // 用来遍历XML文件的结束标签
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        // 判断一个节点是否解析完
        if(qName.equals("book")){
            System.out.println("结束解析第" + bookIndex + "个节点...");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String text = new String(ch, start, length);
        if(!text.trim().equals("")){
            System.out.println(text);
        }

        if("title".equals(currentTag)) {
            System.out.println("title=======" + text);
        }else if("price".equalsIgnoreCase(currentTag)){
            System.out.println("price=======" + text);
        }
        currentTag = null;
    }


    public static void main(String[] args) {
        String path = "F:\\datafortag\\book.xml";
        try {
            // 通过SAXParserFactory的静态方法newInstance()方法获取SAXParserFactory实例对象factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 通过SAXParserFactory实例的newSAXParser()方法返回SAXParser实例parser
            SAXParser saxParser = factory.newSAXParser();
            // 定义SAXParserHandler对象
            SaxParserXmlCode handler = new SaxParserXmlCode();
            // 解析XML文档
            saxParser.parse(path, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
