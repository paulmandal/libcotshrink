package com.paulmandal.atak.libcotshrink.exi;

import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.main.api.sax.EXIResult;
import com.siemens.ct.exi.main.api.sax.EXISource;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public class ExiConverterFactory {
    public ExiConverter createExiConverter() {
        EXIFactory exiFactory = DefaultEXIFactory.newInstance();

        try {
            EXIResult exiResult = new EXIResult(exiFactory);
            EXISource exiSource = new EXISource(exiFactory);

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser newSAXParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = newSAXParser.getXMLReader();

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            return new ExiConverter(exiResult, exiSource, xmlReader, transformer);
        } catch (EXIException | SAXException | TransformerConfigurationException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
