package net.dvdplay.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.dvdplay.exception.DvdplayException;
import org.apache.crimson.tree.XmlDocument;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public final class DataPacketComposer {
   private static final int BLKSIZE = 512;
   private String mEncodingFormat = "UTF-16";
   private DocumentBuilderFactory mDocumentBuilderFactory = null;
   private DocumentBuilder mDocumentBuilder = null;

   public DataPacketComposer() throws DvdplayException {
      try {
         this.mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
         this.mDocumentBuilderFactory.setValidating(false);
         this.mDocumentBuilder = this.mDocumentBuilderFactory.newDocumentBuilder();
      } catch (DvdplayException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new DvdplayException(1007, var4);
      }
   }

   public DataPacketComposer(String encodingFormat) throws DvdplayException {
      try {
         if (!isValidEncodingFormat(encodingFormat)) {
            throw new DvdplayException(3001, "Invalid Encoding Format: [" + encodingFormat + "]");
         } else {
            this.mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.mDocumentBuilderFactory.setValidating(false);
            this.mDocumentBuilder = this.mDocumentBuilderFactory.newDocumentBuilder();
         }
      } catch (DvdplayException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new DvdplayException(1007, var5);
      }
   }

   private static boolean isValidEncodingFormat(String encodingFormat) {
      return encodingFormat == null || encodingFormat == "" ? false : encodingFormat.equalsIgnoreCase("UTF-16") || encodingFormat.equalsIgnoreCase("UTF-8");
   }

   public synchronized String nvMarshal(NvPairSet nvPairs) throws DvdplayException {
      return this.nvMarshal("UTF-16", nvPairs);
   }

   private String removeLineFeed(String original) {
      Pattern p = Pattern.compile("\n");
      Matcher m = p.matcher(original);
      return m.replaceFirst("");
   }

   private synchronized String orig_nvMarshal(String encodingFormat, NvPairSet nvPairs) throws DvdplayException {
      if (!isValidEncodingFormat(encodingFormat)) {
         throw new DvdplayException(3001, "Invalid Encoding Format: [" + encodingFormat + "]");
      } else {
         XmlDocument doc = new XmlDocument();
         OutputFormat format = new OutputFormat(doc);
         format.setEncoding(encodingFormat);
         format.setStandalone(true);
         format.setIndenting(false);
         format.setVersion("1.0");
         StringWriter sw = new StringWriter();

         try {
            Node root = doc.createElement("DVDPLAY-NVPAIR");
            doc.appendChild(root);
            int count = nvPairs.size();
            if (count > 0) {
               for (int i = 0; i < count; i++) {
                  Element entry = doc.createElement("ENTRY");
                  NvPair nvPair = nvPairs.getNvPair(i);
                  entry.setAttribute("name", nvPair.name);
                  entry.setAttribute("type", nvPair.getType());
                  entry.setAttribute("format", nvPair.format);
                  if (nvPair.format.compareToIgnoreCase("STRING") == 0) {
                     entry.setAttribute("value", nvPair.getValueAsString());
                  } else if (nvPair.format.compareToIgnoreCase("RCSET") == 0) {
                     DataPacketComposer internalComposer = new DataPacketComposer(this.mEncodingFormat);
                     RCSet rcSet1 = nvPair.getValueAsRCSet();
                     XmlDocument RCSetDoc = internalComposer.rcMarshalToDoc(rcSet1);
                     DocumentFragment rcFragment = RCSetDoc.createDocumentFragment();
                     Element sourceElement = RCSetDoc.getDocumentElement();
                     Node rcNodeCopy = doc.importNode(sourceElement, true);
                     entry.appendChild(rcNodeCopy);
                  }

                  root.appendChild(entry);
               }
            }

            XMLSerializer serial = new XMLSerializer(sw, format);
            serial.asDOMSerializer();
            serial.serialize(doc.getDocumentElement());
         } catch (DvdplayException var17) {
            throw var17;
         } catch (Exception var18) {
            throw new DvdplayException(3000, var18);
         }

         return sw.toString();
      }
   }

   public synchronized String nvMarshal(String encodingFormat, NvPairSet nvPairs) throws DvdplayException {
      if (!isValidEncodingFormat(encodingFormat)) {
         throw new DvdplayException(3001, "Invalid Encoding Format: [" + encodingFormat + "]");
      } else {
         StringWriter sw = new StringWriter();

         try {
            XmlDocument doc = this.nvMarshal2Doc(encodingFormat, nvPairs);
            OutputFormat format = new OutputFormat(doc);
            format.setEncoding(encodingFormat);
            XMLSerializer serial = new XMLSerializer(sw, format);
            serial.asDOMSerializer();
            serial.serialize(doc.getDocumentElement());
         } catch (DvdplayException var7) {
            throw var7;
         } catch (Exception var8) {
            throw new DvdplayException(3000, var8);
         }

         return sw.toString();
      }
   }

   private synchronized XmlDocument nvMarshal2Doc(String encodingFormat, NvPairSet nvPairs) throws DvdplayException {
      if (!isValidEncodingFormat(encodingFormat)) {
         throw new DvdplayException(3001, "Invalid Encoding Format: [" + encodingFormat + "]");
      } else {
         XmlDocument doc = new XmlDocument();
         OutputFormat format = new OutputFormat(doc);
         format.setEncoding(encodingFormat);
         format.setStandalone(true);
         format.setIndenting(false);
         format.setVersion("1.0");

         try {
            Node root = doc.createElement("DVDPLAY-NVPAIR");
            doc.appendChild(root);
            int count = nvPairs.size();
            if (count > 0) {
               for (int i = 0; i < count; i++) {
                  Element entry = doc.createElement("ENTRY");
                  NvPair nvPair = nvPairs.getNvPair(i);
                  entry.setAttribute("name", nvPair.name);
                  entry.setAttribute("type", nvPair.getType());
                  entry.setAttribute("format", nvPair.format);
                  if (nvPair.format.compareToIgnoreCase("STRING") == 0) {
                     entry.setAttribute("value", nvPair.getValueAsString());
                  } else if (nvPair.format.compareToIgnoreCase("RCSET") == 0) {
                     DataPacketComposer internalComposer = new DataPacketComposer(this.mEncodingFormat);
                     RCSet rcSet1 = nvPair.getValueAsRCSet();
                     XmlDocument RCSetDoc = internalComposer.rcMarshalToDoc(rcSet1);
                     DocumentFragment rcFragment = RCSetDoc.createDocumentFragment();
                     Element sourceElement = RCSetDoc.getDocumentElement();
                     Node rcNodeCopy = doc.importNode(sourceElement, true);
                     entry.appendChild(rcNodeCopy);
                  }

                  root.appendChild(entry);
               }
            }

            return doc;
         } catch (DvdplayException var16) {
            throw var16;
         } catch (Exception var17) {
            throw new DvdplayException(3000, var17);
         }
      }
   }

   public synchronized String nvMarshal(String encodingFormat, String[] names, String[] values, String[] types) throws DvdplayException {
      NvPairSet nvPairs = new NvPairSet(names, types, values);
      return this.nvMarshal(encodingFormat, nvPairs);
   }

   public synchronized String nvMarshal(String[] names, String[] types, String[] values) throws DvdplayException {
      NvPairSet nvPairs = new NvPairSet(names, types, values);
      return this.nvMarshal("UTF-16", nvPairs);
   }

   public synchronized String rcMarshal(String[] names, String[] types, RCSet dataRows) throws DvdplayException {
      return this.rcMarshal("UTF-16", names, types, dataRows);
   }

   synchronized XmlDocument rcMarshalToDoc(RCSet rcSet) throws DvdplayException {
      String[] names = rcSet.getNames();
      String[] types = rcSet.getTypes();
      return this.rcMarshalToDoc("UTF-16", names, types, rcSet);
   }

   public synchronized String rcMarshal(String encodingFormat, String[] names, String[] types, RCSet dataRows) throws DvdplayException {
      XmlDocument doc = null;
      StringWriter sw = new StringWriter();

      try {
         doc = this.rcMarshalToDoc(encodingFormat, names, types, dataRows);
         OutputFormat format = new OutputFormat(doc);
         format.setEncoding(encodingFormat);
         format.setStandalone(true);
         format.setIndenting(false);
         format.setVersion("1.0");
         XMLSerializer serial = new XMLSerializer(sw, format);
         serial.asDOMSerializer();
         serial.serialize(doc.getDocumentElement());
      } catch (DvdplayException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new DvdplayException(3000, var10);
      }

      return sw.toString();
   }

   synchronized XmlDocument rcMarshalToDoc(String encodingFormat, String[] names, String[] types, RCSet dataRows) throws DvdplayException {
      XmlDocument doc = new XmlDocument();
      String ROW_TAG_NAME = "n";
      String ROW_TAG_VALUE = "v";

      try {
         if (names.length != types.length) {
            throw new DvdplayException(3001, "Marshaling name/types length mismatch");
         } else {
            Node root = doc.createElement("DVDPLAY-RCSET");
            doc.appendChild(root);
            int count = names.length;
            if (count > 0) {
               Node metaRow = doc.createElement("META-ROW");
               root.appendChild(metaRow);

               for (int i = 0; i < count; i++) {
                  Element metaField = doc.createElement("META-FIELD");
                  metaField.setAttribute("name", names[i]);
                  metaField.setAttribute("type", types[i]);
                  metaRow.appendChild(metaField);
               }

               int rowCount = dataRows.rowCount();

               for (int i = 0; i < rowCount; i++) {
                  if (!dataRows.isDeleted(i)) {
                     RDataSetFieldValues aRow = dataRows.getRow(i);
                     Node rowNode = doc.createElement("ROW");
                     root.appendChild(rowNode);
                     int rowFieldCount = aRow.getCount();

                     for (int j = 0; j < rowFieldCount; j++) {
                        Element fieldNode = doc.createElement("FIELD");
                        fieldNode.setAttribute("v", dataRows.getFieldValue(i, j));
                        rowNode.appendChild(fieldNode);
                     }
                  }
               }
            }

            return doc;
         }
      } catch (Exception var19) {
         throw new DvdplayException(3000, var19);
      }
   }

   private NvPairSet processNV_Entry(NodeList elements) throws DvdplayException {
      NvPairSet nvFound = new NvPairSet();
      if (elements == null) {
         return nvFound;
      } else {
         int elementCount = elements.getLength();
         NvPair nv = null;

         for (int i = 0; i < elementCount; i++) {
            Element entry = (Element)elements.item(i);
            String name = entry.getAttribute("name");
            String type = entry.getAttribute("type");
            String format = entry.getAttribute("format");
            if ("STRING".compareToIgnoreCase(format) == 0) {
               String value = entry.getAttribute("value");
               nv = new NvPair(name, type, value);
            } else if ("RCSET".compareToIgnoreCase(format) == 0) {
               NodeList nodeList = entry.getElementsByTagName("DVDPLAY-RCSET");
               Element rcSetElement = (Element)nodeList.item(0);
               RCSet value = this.processRCSET_Element(rcSetElement);
               nv = new NvPair(name, type, value);
            }

            nvFound.add(nv);
         }

         return nvFound;
      }
   }

   private RMetaRow processRC_MetaFields(NodeList elements) throws DvdplayException {
      RMetaRow metaRow = new RMetaRow();
      if (elements == null) {
         return metaRow;
      } else {
         int elementCount = elements.getLength();

         for (int i = 0; i < elementCount; i++) {
            Element entry = (Element)elements.item(i);
            String name = entry.getAttribute("name");
            String type = entry.getAttribute("type");
            metaRow.addMetaField(name, type);
         }

         return metaRow;
      }
   }

   private void processRC_Row(NodeList elements, RCSet dataSet) throws DvdplayException {
      String TAG_NAME = "n";
      String TAG_VALUE = "v";
      if (elements != null) {
         RDataSetFieldValues fieldValues = new RDataSetFieldValues(dataSet.fieldCount());
         int elementCount = elements.getLength();

         for (int i = 0; i < elementCount; i++) {
            Element entry = (Element)elements.item(i);
            NodeList allFields = entry.getElementsByTagName("FIELD");
            int fieldCount = allFields.getLength();

            for (int j = 0; j < fieldCount; j++) {
               Element fieldElement = (Element)allFields.item(j);
               String value = fieldElement.getAttribute("v");
               fieldValues.setValue(j, value);
            }

            dataSet.addRow(fieldValues);
         }
      }
   }

   public synchronized NvPairSet nvDeMarshal(String xmlString) {
      new NvPairSet();

      try {
         InputSource is = new InputSource(new StringReader(xmlString));
         Document document = this.mDocumentBuilder.parse(is);
         String elementName = "ENTRY";
         NodeList elements = document.getElementsByTagName(elementName);
         return this.processNV_Entry(elements);
      } catch (DvdplayException var7) {
         throw var7;
      } catch (Exception var8) {
         throw new DvdplayException(3001, var8);
      }
   }

   private synchronized NvPairSet nvDeMarshalFromFile(String fileName) {
      new NvPairSet();

      try {
         Document document = this.mDocumentBuilder.parse(fileName);
         NodeList elements = document.getElementsByTagName("ENTRY");
         return this.processNV_Entry(elements);
      } catch (DvdplayException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new DvdplayException(3001, var6);
      }
   }

   private synchronized RCSet processRCSET_Element(Element rcSetElement) {
      RCSet dataSet = null;

      try {
         String elementName = "META-FIELD";
         NodeList elements = rcSetElement.getElementsByTagName(elementName);
         RMetaRow metaRow = this.processRC_MetaFields(elements);
         dataSet = new RCSet(metaRow);
         String rowElement = "ROW";
         NodeList rowElements = rcSetElement.getElementsByTagName(rowElement);
         this.processRC_Row(rowElements, dataSet);
      } catch (DvdplayException var13) {
         throw var13;
      } catch (Exception var14) {
         throw new DvdplayException(3001, var14);
      }

      return dataSet;
   }

   public synchronized RCSet rcDeMarshal(String xmlString) {
      RCSet dataSet = null;

      try {
         InputSource is = new InputSource(new StringReader(xmlString));
         Document document = this.mDocumentBuilder.parse(is);
         String elementName = "META-FIELD";
         NodeList elements = document.getElementsByTagName(elementName);
         RMetaRow metaRow = this.processRC_MetaFields(elements);
         dataSet = new RCSet(metaRow);
         String rowElement = "ROW";
         NodeList rowElements = document.getElementsByTagName(rowElement);
         this.processRC_Row(rowElements, dataSet);
      } catch (DvdplayException var15) {
         throw var15;
      } catch (Exception var16) {
         throw new DvdplayException(3001, var16);
      }

      return dataSet;
   }

   private synchronized RCSet rcDeMarshalFromFile(String fileName) {
      RCSet dataSet = null;

      try {
         Document document = this.mDocumentBuilder.parse(fileName);
         String elementName = "META-FIELD";
         NodeList elements = document.getElementsByTagName(elementName);
         RMetaRow metaRow = this.processRC_MetaFields(elements);
         dataSet = new RCSet(metaRow);
         String rowElement = "ROW";
         NodeList rowElements = document.getElementsByTagName(rowElement);
         this.processRC_Row(rowElements, dataSet);
      } catch (DvdplayException var14) {
         throw var14;
      } catch (Exception var15) {
         throw new DvdplayException(3001, var15);
      }

      return dataSet;
   }

   public String prettyPrint(String xmlString) {
      String retVal = "";

      try {
         InputSource is = new InputSource(new StringReader(xmlString));
         Document document = this.mDocumentBuilder.parse(is);
         OutputFormat format = new OutputFormat();
         format.setEncoding("UTF-16");
         format.setLineSeparator("\r\n");
         format.setLineWidth(0);
         format.setIndenting(true);
         format.setIndent(2);
         StringWriter sw = new StringWriter();
         XMLSerializer serializer = new XMLSerializer(sw, format);
         serializer.serialize(document);
         return sw.toString();
      } catch (DvdplayException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new DvdplayException(3001, var9);
      }
   }

   public static void save(String xmlString, String fileName) throws DvdplayException {
      try {
         BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-16"));
         wr.write(xmlString);
         wr.close();
      } catch (DvdplayException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new DvdplayException(3001, var5);
      }
   }

   public static String load(String fileName) throws DvdplayException {
      try {
         BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-16"));
         StringBuffer sb = new StringBuffer();
         char[] b = new char[512];
         int n = 0;

         while ((n = br.read(b)) > 0) {
            sb.append(b, 0, n);
         }

         br.close();
         return sb.toString();
      } catch (DvdplayException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new DvdplayException(3001, var6);
      }
   }
}
