// This class was generated by the JAXRPC RI, do not edit.
// Contents subject to change without notice.

package com.msaf.validador.wsclient;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.ArrayList;

public class ConsultaCliente_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable {
    private static final QName ns1_arg0_QNAME = new QName("", "arg0");
    private static final QName ns2_string_TYPE_QNAME = SchemaConstants.QNAME_TYPE_STRING;
    private CombinedSerializer myns2_string__java_lang_String_String_Serializer;
    private static final QName ns1_arg1_QNAME = new QName("", "arg1");
    private static final QName ns2_int_TYPE_QNAME = SchemaConstants.QNAME_TYPE_INT;
    private CombinedSerializer myns2__int__int_Int_Serializer;
    private static final QName ns1_arg2_QNAME = new QName("", "arg2");
    private static final QName ns1_arg3_QNAME = new QName("", "arg3");
    
    public ConsultaCliente_LiteralSerializer(QName type, String encodingStyle) {
        super(type, true, encodingStyle);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        myns2_string__java_lang_String_String_Serializer = (CombinedSerializer)registry.getSerializer("", java.lang.String.class, ns2_string_TYPE_QNAME);
        myns2__int__int_Int_Serializer = (CombinedSerializer)registry.getSerializer("", int.class, ns2_int_TYPE_QNAME);
    }
    
    public Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws Exception {
        com.msaf.validador.wsclient.ConsultaCliente instance = new com.msaf.validador.wsclient.ConsultaCliente();
        Object member;
        QName elementName;
        List values;
        Object value;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_arg0_QNAME)) {
                member = myns2_string__java_lang_String_String_Serializer.deserialize(ns1_arg0_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setArg0((java.lang.String)member);
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_arg1_QNAME)) {
                member = myns2__int__int_Int_Serializer.deserialize(ns1_arg1_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setArg1(((Integer)member).intValue());
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_arg2_QNAME)) {
                member = myns2__int__int_Int_Serializer.deserialize(ns1_arg2_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setArg2(((Integer)member).intValue());
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_arg3_QNAME)) {
                member = myns2_string__java_lang_String_String_Serializer.deserialize(ns1_arg3_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setArg3((java.lang.String)member);
                reader.nextElementContent();
            }
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (Object)instance;
    }
    
    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.msaf.validador.wsclient.ConsultaCliente instance = (com.msaf.validador.wsclient.ConsultaCliente)obj;
        
    }
    public void doSerialize(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.msaf.validador.wsclient.ConsultaCliente instance = (com.msaf.validador.wsclient.ConsultaCliente)obj;
        
        if (instance.getArg0() != null) {
            myns2_string__java_lang_String_String_Serializer.serialize(instance.getArg0(), ns1_arg0_QNAME, null, writer, context);
        }
        myns2__int__int_Int_Serializer.serialize(new Integer(instance.getArg1()), ns1_arg1_QNAME, null, writer, context);
        myns2__int__int_Int_Serializer.serialize(new Integer(instance.getArg2()), ns1_arg2_QNAME, null, writer, context);
        if (instance.getArg3() != null) {
            myns2_string__java_lang_String_String_Serializer.serialize(instance.getArg3(), ns1_arg3_QNAME, null, writer, context);
        }
    }
}
