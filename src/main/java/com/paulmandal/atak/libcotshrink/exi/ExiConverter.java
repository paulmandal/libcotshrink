package com.paulmandal.atak.libcotshrink.exi;

import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.main.api.sax.EXIResult;
import com.siemens.ct.exi.main.api.sax.EXISource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

public class ExiConverter {
    private EXIResult mExiResult;
    private EXISource mExiSource;
    private XMLReader mXmlReader;
    private Transformer mTransformer;

    public ExiConverter(EXIResult exiResult,
                        EXISource exiSource,
                        XMLReader xmlReader,
                        Transformer transformer) {
        mExiResult = exiResult;
        mExiSource = exiSource;
        mXmlReader = xmlReader;
        mTransformer = transformer;
    }

    public byte[] xmlToExi(String xml) {
        byte[] exiBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes())) {
            mExiResult.setOutputStream(baos);
            mXmlReader.setContentHandler(mExiResult.getHandler());
            InputSource inputSource = new InputSource(bais);
            mXmlReader.parse(inputSource);
            exiBytes = baos.toByteArray();
        } catch (IOException | EXIException | SAXException e) {
            e.printStackTrace();
            return new byte[0];
        }
        return exiBytes;
    }

    public String exiToXml(byte[] exi) {
        String xml;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(exi);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            InputSource is = new InputSource(bais);
            mExiSource.setInputSource(is);

            Result result = new StreamResult(baos);
            mTransformer.transform(mExiSource, result);
            xml = baos.toString();
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
            return null;
        }

        return xml;
    }
}
