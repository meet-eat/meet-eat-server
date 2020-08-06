package meet_eat.server;

import meet_eat.data.ObjectJsonParser;

import java.beans.PropertyEditorSupport;

public class HeaderPropertyEditor extends PropertyEditorSupport {

    private final Class<?> headerClass;

    public HeaderPropertyEditor(Class<?> headerClass) {
        this.headerClass = headerClass;
    }

    @Override
    public String getAsText() {
        return new ObjectJsonParser().parseObjectToJsonString(getValue());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(new ObjectJsonParser().parseJsonStringToObject(text, headerClass));
    }
}
