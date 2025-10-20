/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.babelfish;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import oracle.jdbc.babelfish.TranslatedErrorInfo;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.Supports;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.SQL_TRANSLATION})
@DefaultLevel(value=Logging.FINEST)
class TranslationCache {
    private Map<String, String> queryCache = new ConcurrentHashMap<String, String>();
    private Map<Integer, TranslatedErrorInfo> errorCache = new ConcurrentHashMap<Integer, TranslatedErrorInfo>();
    private Map<Integer, TranslatedErrorInfo> localErrorCache;

    TranslationCache(File file) throws SQLException {
        if (file != null) {
            this.localErrorCache = new ConcurrentHashMap<Integer, TranslatedErrorInfo>();
            this.readLocalErrorFile(file);
        }
    }

    private void readLocalErrorFile(File file) throws SQLException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            documentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            int n2 = 0;
            TranslatedErrorInfo translatedErrorInfo = null;
            NodeList nodeList = document.getElementsByTagName("Exception");
            for (int i2 = 0; i2 < nodeList.getLength(); ++i2) {
                translatedErrorInfo = new TranslatedErrorInfo();
                Node node = nodeList.item(i2);
                NodeList nodeList2 = node.getChildNodes();
                for (int i3 = 0; i3 < nodeList2.getLength(); ++i3) {
                    if (nodeList2.item(i3).getNodeType() != 1) continue;
                    Element element = (Element)nodeList2.item(i3);
                    if (element.getTagName().equals("ORAError")) {
                        n2 = Integer.parseInt(element.getFirstChild().getNodeValue());
                        continue;
                    }
                    if (element.getTagName().equals("ErrorCode")) {
                        translatedErrorInfo.setErrorCode(Integer.parseInt(element.getFirstChild().getNodeValue()));
                        continue;
                    }
                    if (!element.getTagName().equals("SQLState")) continue;
                    translatedErrorInfo.setSqlState(element.getFirstChild().getNodeValue());
                }
                this.localErrorCache.put(n2, translatedErrorInfo);
                n2 = 0;
            }
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(277).fillInStackTrace();
        }
        catch (SAXException sAXException) {
            throw (SQLException)DatabaseError.createSqlException(278).fillInStackTrace();
        }
        catch (ParserConfigurationException parserConfigurationException) {
            throw (SQLException)DatabaseError.createSqlException(278).fillInStackTrace();
        }
    }

    Map<String, String> getQueryCache() {
        return this.queryCache;
    }

    Map<Integer, TranslatedErrorInfo> getErrorCache() {
        return this.errorCache;
    }

    Map<Integer, TranslatedErrorInfo> getLocalErrorCache() {
        return this.localErrorCache;
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }
}

