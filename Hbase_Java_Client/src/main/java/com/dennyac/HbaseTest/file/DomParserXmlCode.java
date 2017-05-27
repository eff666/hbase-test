package com.dennyac.HbaseTest.file;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shuyun on 2017/3/30.
 */
public class DomParserXmlCode {

    /**
     * 1、通过DOM（org.w3c.dom）解析xml文件，读取内容
     * @param filePath
     */
    public static void readXmlCodeByDom(String filePath){
        try {
            // 创建一个DocumentBuilderFactory对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 创建DocumentBuilder对象
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 解析XML文件获取Document对象（注意：org.w3c.dom.Document）
            Document document = builder.parse(filePath);
            // 获取所有book元素节点集合
            NodeList bookList = document.getElementsByTagName("book");
            // 遍历每一个book元素节点
            int size = bookList.getLength();
            System.out.println("一共有" + size + "个节点...");
            // 元素节点
            for(int i = 0;i < size;++i){
                // 通过item方法获取每一个book元素节点
                Node bookNode = bookList.item(i);
                // 获取book元素节点所有属性节点集合
                NamedNodeMap attriMap = bookNode.getAttributes();
                System.out.println("第" + (i+1) + "个节点：");
                int attriSize = attriMap.getLength();
                System.out.println("---共有" + attriSize + "个属性");
                // 属性
                for(int j = 0;j < attriSize;++j){
                    // 通过item方法获取元素节点的属性节点
                    Node attriNode = attriMap.item(j);
                    // 获取属性节点属性类型
                    System.out.print("------type:" + attriNode.getNodeType());
                    // 获取属性节点属性名称
                    System.out.print("   name:" + attriNode.getNodeName());
                    // 获取属性节点属性值
                    System.out.println("   value:" + attriNode.getNodeValue());
                }
                // 如果知道元素节点有几个属性
				/*Element element = (Element)bookList.item(i);
				String attriValue = element.getAttribute("category");
				System.out.println("   value:" + attriValue);*/
                // 获取book元素节点的子节点集合
                NodeList childNodeList = bookNode.getChildNodes();
                int childSize = childNodeList.getLength();
                System.out.println("---共有" + childSize + "个子节点（元素节点和文本节点）：");
                for(int k = 0;k < childSize; ++k){
                    // 获取子节点
                    Node childNode = childNodeList.item(k);
                    // 区分Elemet类型节点和Text类型节点
                    if(childNode.getNodeType() == Node.ELEMENT_NODE){
                        // 获取元素子节点类型
                        System.out.print("------type:" + childNode.getNodeType());
                        // 获取元素子节点名称
                        System.out.print("   name:" + childNode.getNodeName());
                        // 获取元素子节点值
                        System.out.print("   value:" + childNode.getNodeValue());
                        /**
                         * <title lang="chi">Java编程思想</title> 对于这个被视为一个元素节点（childNode），
                         * 之所以元素节点值返回null，是因为"Java编程思想"被视为该元素节点的一个子节点，而不是我们误以为的元素值。
                         * 我们要得到这本文本值，可以用：
                         * childNode.getFirstChild().getNodeName()
                         * 或者
                         * childNode.getTextContent()
                         */
                        System.out.print("   value:" + childNode.getTextContent());

                        // 我们误以为是子节点的值 其是是子节点的一个文本节点
                        System.out.print("   （sub-name:" + childNode.getFirstChild().getNodeName());
                        System.out.print("   sub-type:" + childNode.getFirstChild().getNodeType());
                        System.out.println("   sub-value:" + childNode.getFirstChild().getNodeValue() + "）");
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void outputToXml(Node node, String filePath){
        try {
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            // 设置各种输出属性
            transformer.setOutputProperty("encoding", "utf-8");
            transformer.setOutputProperty("indent", "yes");
            DOMSource source = new DOMSource();
            // 将待转换输出节点赋值给DOM源模型的持有者(holder)
            source.setNode(node);
            StreamResult result = new StreamResult();
            if (filePath == null) {
                // 设置标准输出流为transformer的底层输出目标
                result.setOutputStream(System.out);
            } else {
                result.setOutputStream(new FileOutputStream(filePath));
            }
            // 执行转换从源模型到控制台输出流
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 2、通过DOM向XML文件中添加节点
     * @param filePath
     */
    public static void insertXmlCodeByDom(String filePath){
        try {
            // 创建一个DocumentBuilderFactory对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 创建DocumentBuilder对象
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 解析XML文件获取Document对象（注意：org.w3c.dom.Document）
            Document document = builder.parse(filePath);
            // 获取根节点
            Element rootNode = document.getDocumentElement();

            // 1. 添加节点book
            Element bookNode = document.createElement("book");
            // 添加属性
            bookNode.setAttribute("category", "Docker");

            // 添加节点title
            Element titleNode = document.createElement("title");
            // 添加属性(第二种方法:先创建一个属性节点，设置名称和值)
            Attr langAttr = document.createAttribute("lang");
            langAttr.setNodeValue("web");
            titleNode.setAttributeNode(langAttr);
            // 设置文本内容
            titleNode.setTextContent("The Docker Book");
            // 设置为bookNode的子节点
            bookNode.appendChild(titleNode);

            // 添加节点author
            Element authorNode = document.createElement("author");
            // 设置文本内容
            authorNode.setTextContent("James Turnbull");
            // 设置为bookNode的子节点
            bookNode.appendChild(authorNode);

            // 添加节点price
            Element priceNode = document.createElement("price");
            // 设置文本内容
            priceNode.setTextContent("59.00");
            // 设置为bookNode的子节点
            bookNode.appendChild(priceNode);

            // bookNode节点插入节点树中
            rootNode.appendChild(bookNode);

            outputToXml(rootNode,filePath);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 3、通过DOM修改xml文件内容
     * @param filePath
     */
    public static void updateXmlCodeByDom(String filePath){
        try {
            // 创建一个DocumentBuilderFactory对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 创建DocumentBuilder对象
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 解析XML文件获取Document对象（注意：org.w3c.dom.Document）
            Document document = builder.parse(filePath);
            // 获取根节点
            Element rootNode = document.getDocumentElement();

            // book集合
            NodeList bookList = document.getElementsByTagName("book");
            // 修改第四个节点
            Node bookNode = bookList.item(3);
            // 修改属性
            NamedNodeMap attrMap = bookNode.getAttributes();
            Node attrNode = attrMap.item(0);
            attrNode.setNodeValue("Hadoop");

            // title子节点集合
            NodeList titleList = document.getElementsByTagName("title");
            // title子节点
            Node titleNode = titleList.item(3);
            titleNode.setTextContent("Hadoop权威指南");

            // author子节点集合
            NodeList authorList = document.getElementsByTagName("author");
            // author子节点
            Node authorNode = authorList.item(3);
            authorNode.setTextContent("周敏奇");

            // price子节点集合
            NodeList priceList = document.getElementsByTagName("price");
            // price子节点
            Node priceNode = priceList.item(3);
            priceNode.setTextContent("79");

            outputToXml(rootNode,filePath);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 4、删除节点
     * @param filePath
     */
    public static void deleteXmlCodeByDom(String filePath){
        try {
            // 创建一个DocumentBuilderFactory对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 创建DocumentBuilder对象
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 解析XML文件获取Document对象（注意：org.w3c.dom.Document）
            Document document = builder.parse(filePath);
            // 获取根节点
            Element rootNode = document.getDocumentElement();

            // book集合
            NodeList bookList = document.getElementsByTagName("book");

            // 删除第三个节点的year属性
            Node book3Node = bookList.item(2);
            // year节点集合
            NodeList yearList = document.getElementsByTagName("year");
            // 删除第三个节点的第三个子节点（year）
            book3Node.removeChild(yearList.item(2));

            // 第四个节点所有
            Node book4Node = bookList.item(3);
            // 删除第四个节点
            rootNode.removeChild(book4Node);

            outputToXml(rootNode,filePath);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String filePath = "F:\\datafortag\\book.xml";
        //readXmlCodeByDom("F:\\datafortag\\book.xml");
        //insertXmlCodeByDom("F:\\datafortag\\book.xml");

        //updateXmlCodeByDom(filePath);

        //deleteXmlCodeByDom(filePath);
    }
}
