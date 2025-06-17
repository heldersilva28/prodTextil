package com.ipvc.desktop.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Utilitário para exibir diferentes tipos de diálogos na aplicação.
 * Esta classe facilita a exibição de mensagens de informação, erro, aviso e confirmação.
 */
public class DialogUtils {

    /**
     * Exibe um diálogo de informação.
     *
     * @param title Título do diálogo
     * @param header Cabeçalho do diálogo
     * @param content Conteúdo do diálogo
     */
    public static void showInformationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Exibe um diálogo de erro.
     *
     * @param title Título do diálogo
     * @param header Cabeçalho do diálogo
     * @param content Conteúdo do diálogo
     */
    public static void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Exibe um diálogo de aviso.
     *
     * @param title Título do diálogo
     * @param header Cabeçalho do diálogo
     * @param content Conteúdo do diálogo
     */
    public static void showWarningDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Exibe um diálogo de confirmação e retorna se o usuário confirmou ou não.
     *
     * @param title Título do diálogo
     * @param header Cabeçalho do diálogo
     * @param content Conteúdo do diálogo
     * @return true se o usuário confirmou, false caso contrário
     */
    public static boolean showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Exibe um diálogo personalizado com os botões especificados.
     *
     * @param title Título do diálogo
     * @param header Cabeçalho do diálogo
     * @param content Conteúdo do diálogo
     * @param alertType Tipo de alerta
     * @param buttonTypes Tipos de botões a serem exibidos
     * @return O botão que foi pressionado pelo usuário
     */
    public static Optional<ButtonType> showCustomDialog(String title, String header, String content,
                                                      Alert.AlertType alertType, ButtonType... buttonTypes) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        if (buttonTypes != null && buttonTypes.length > 0) {
            alert.getButtonTypes().setAll(buttonTypes);
        }

        return alert.showAndWait();
    }
}
