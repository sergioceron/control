package mx.ipn.cidetec.virtual.utils;

import com.sun.faces.renderkit.html_basic.FormRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import java.io.IOException;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 4/08/14 10:38 PM
 */
public class CustomFormRenderer extends FormRenderer {

    private static final String[] attributes = new String[]{
			"data-parsley-validate"
	};

    public CustomFormRenderer() {
        super();
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        final ResponseWriter originalResponseWriter = context.getResponseWriter();
        context.setResponseWriter(new ResponseWriterWrapper() {

            @Override
            public ResponseWriter getWrapped() {
                return originalResponseWriter;
            }

            @Override
            public void startElement(String name, UIComponent component) throws IOException {
                super.startElement(name, component);
                if ("form".equalsIgnoreCase(name)) {
                    for (String attr : attributes) {
                        final String value = (String) component.getAttributes().get(attr);
                        if (value != null) {
                            super.writeAttribute(attr, value, attr);
                        }
                    }
                }
            }
        });
        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
    }

    @Override
	protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue) throws IOException {
        super.getEndTextToRender(context, component, currentValue);
	}
}
