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

public class ToSnakeHandler extends AbstractHandler {

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
                // toSnake 함수 호출
                String snakeCaseText = toSnake(selectedText);

                // 텍스트를 교체함
                try {
                    textEditor.getDocumentProvider().getDocument(editor.getEditorInput())
                            .replace(selection.getOffset(), selection.getLength(), snakeCaseText);
                } catch (BadLocationException e) {
                    throw new ExecutionException("Error replacing text", e);
                }
            }
        }
        return null;
    }

    // toSnake 함수 구현 (카멜 케이스를 스네이크 케이스로 변환)
    private String toSnake(String text) {
        // 첫 글자를 소문자로 변환하는 정규식 패턴
        Pattern firstPattern = Pattern.compile("^([A-Z])");
        Matcher firstMatcher = firstPattern.matcher(text);
        text = firstMatcher.replaceFirst(m -> m.group(1).toLowerCase());

        // 카멜 케이스의 대문자를 _ 소문자로 변환하는 정규식 패턴
        Pattern camelPattern = Pattern.compile("([A-Z])");
        Matcher camelMatcher = camelPattern.matcher(text);

        StringBuffer result = new StringBuffer();
        while (camelMatcher.find()) {
            camelMatcher.appendReplacement(result, "_" + camelMatcher.group(1).toLowerCase());
        }
        camelMatcher.appendTail(result);

        return result.toString().toUpperCase();  // 전체를 대문자로 변환하여 반환
    }
}
