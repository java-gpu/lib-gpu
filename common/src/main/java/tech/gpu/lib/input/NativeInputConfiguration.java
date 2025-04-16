package tech.gpu.lib.input;

import lombok.Getter;
import tech.gpu.lib.Input;

public class NativeInputConfiguration {

    @Getter
    private Input.OnscreenKeyboardType type = Input.OnscreenKeyboardType.Default;
    @Getter
    private boolean preventCorrection = false;

    @Getter
    private TextInputWrapper textInputWrapper;
    private boolean isMultiLine = false;
    @Getter
    private Integer maxLength;
    @Getter
    private Input.InputStringValidator validator;
    @Getter
    private String placeholder = "";
    @Getter
    private boolean showPasswordButton = false;
    @Getter
    private String[] autoComplete = null;

    /**
     * @param type which type of keyboard we wish to display.
     */
    public NativeInputConfiguration setType(Input.OnscreenKeyboardType type) {
        this.type = type;
        return this;
    }

    /**
     * @param preventCorrection Disable autocomplete/correction
     */
    public NativeInputConfiguration setPreventCorrection(boolean preventCorrection) {
        this.preventCorrection = preventCorrection;
        return this;
    }

    /**
     * @param textInputWrapper Should provide access to the backed input field.
     */
    public NativeInputConfiguration setTextInputWrapper(TextInputWrapper textInputWrapper) {
        this.textInputWrapper = textInputWrapper;
        return this;
    }

    public boolean isMultiLine() {
        return isMultiLine;
    }

    /**
     * @param multiLine whether the keyboard should accept multiple lines.
     */
    public NativeInputConfiguration setMultiLine(boolean multiLine) {
        isMultiLine = multiLine;
        return this;
    }

    /**
     * @param maxLength What the text length limit should be.
     */
    public NativeInputConfiguration setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    /**
     * @param validator Can validate the input from the keyboard and reject.
     */
    public NativeInputConfiguration setValidator(Input.InputStringValidator validator) {
        this.validator = validator;
        return this;
    }

    /**
     * @param placeholder String to show, if nothing is inputted yet.
     */
    public NativeInputConfiguration setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    /**
     * @param showPasswordButton Whether to show a button to show unhidden password
     */
    public NativeInputConfiguration setShowPasswordButton(boolean showPasswordButton) {
        this.showPasswordButton = showPasswordButton;
        return this;
    }

    public NativeInputConfiguration setAutoComplete(String[] autoComplete) {
        this.autoComplete = autoComplete;
        return this;
    }

    public void validate() {
        String message = null;
        if (type == null) message = "OnscreenKeyboardType needs to be non null";
        if (textInputWrapper == null) message = "TextInputWrapper needs to be non null";
        if (showPasswordButton && type != Input.OnscreenKeyboardType.Password)
            message = "ShowPasswordButton only works with OnscreenKeyboardType.Password";
        if (placeholder == null) message = "Placeholder needs to be non null";
        if (autoComplete != null && type != Input.OnscreenKeyboardType.Default)
            message = "AutoComplete should only be used with OnscreenKeyboardType.Default";
        if (autoComplete != null && isMultiLine) message = "AutoComplete shouldn't be used with multiline";

        if (message != null) {
            throw new IllegalArgumentException("NativeInputConfiguration validation failed: " + message);
        }
    }
}
