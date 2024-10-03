package ramoi.util.handlers;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public class ToCamelHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // 현재 활성화된 에디터를 가져옴
        IEditorPart editor = HandlerUtil.getActiveEditor(event);
        if (editor instanceof ITextEditor) {
            ITextEditor textEditor = (ITextEditor) editor;
            ITextSelection selection = (ITextSelection) textEditor.getSelectionProvider().getSelection();

            // 선택된 텍스트를 가져옴
            String selectedText = selection.getText();
            if (selectedText != null && !selectedText.isEmpty()) {
                // toCamel 함수 호출
                String camelCaseText = toCamel(selectedText.toLowerCase());

                // 텍스트를 교체함
                try {
                    textEditor.getDocumentProvider().getDocument(editor.getEditorInput())
                            .replace(selection.getOffset(), selection.getLength(), camelCaseText);
                } catch (BadLocationException e) {
                    throw new ExecutionException("Error replacing text", e);
                }
            }
        }
        return null;
    }

    // toCamel 함수 구현 (스네이크 케이스를 카멜 케이스로 변환)
    private String toCamel(String text) {
        Pattern pattern = Pattern.compile("_(.)");
        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
