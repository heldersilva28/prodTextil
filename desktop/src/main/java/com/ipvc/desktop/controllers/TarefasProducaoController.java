package com.ipvc.desktop.controllers;

import com.ipvc.desktop.models.EncomendaCliente;
import com.ipvc.desktop.models.EstadoEncomenda;
import com.ipvc.desktop.models.EtapaDTO;
import com.ipvc.desktop.models.FuncionarioProducaoDTO;
import com.ipvc.desktop.models.TarefaDTO;
import com.ipvc.desktop.services.ProducaoService;
import com.ipvc.desktop.services.UtilizadorService;
import com.ipvc.desktop.utils.DialogUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class TarefasProducaoController implements Initializable {

    @FXML
    private ComboBox<FuncionarioProducaoDTO> funcionarioComboBox;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnNovaTarefa;

    @FXML
    private Button btnVoltar;

    @FXML
    private VBox encomendaInfoContainer;

    @FXML
    private TableView<EncomendaCliente> encomendasTableView;

    @FXML
    private TableColumn<EncomendaCliente, Integer> colId;

    @FXML
    private TableColumn<EncomendaCliente, String> colCliente;

    @FXML
    private TableColumn<EncomendaCliente, LocalDate> colDataEncomenda;

    @FXML
    private TableColumn<EncomendaCliente, BigDecimal> colPrecoTotal;

    @FXML
    private TableColumn<EncomendaCliente, String> colEstado;

    @FXML
    private TableColumn<EncomendaCliente, Void> colAcoes;

    @FXML
    private VBox tarefasContainer;

    @FXML
    private TableView<TarefaDTO> tarefasTableView;

    @FXML
    private TableColumn<TarefaDTO, Integer> colTarefaId;

    @FXML
    private TableColumn<TarefaDTO, String> colEncomendaId;

    @FXML
    private TableColumn<TarefaDTO, String> colDescricao;

    @FXML
    private TableColumn<TarefaDTO, LocalDate> colDataInicio;

    @FXML
    private TableColumn<TarefaDTO, LocalDate> colDataFim;

    @FXML
    private TableColumn<TarefaDTO, String> colStatus;

    @FXML
    private TableColumn<TarefaDTO, Void> colTarefaAcoes;

    @FXML
    private VBox etapasContainer;

    @FXML
    private TableView<EtapaDTO> etapasTableView;

    @FXML
    private TableColumn<EtapaDTO, Integer> colEtapaId;

    @FXML
    private TableColumn<EtapaDTO, Integer> colTarefaRef;

    @FXML
    private TableColumn<EtapaDTO, String> colEtapaDescricao;

    @FXML
    private TableColumn<EtapaDTO, LocalDate> colEtapaDataInicio;

    @FXML
    private TableColumn<EtapaDTO, LocalDate> colEtapaDataFim;


    @FXML
    private TableColumn<EtapaDTO, Void> colEtapaAcoes;

    private final UtilizadorService utilizadorService = new UtilizadorService();
    private final ProducaoService producaoService = new ProducaoService();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ObservableList<FuncionarioProducaoDTO> funcionariosList = FXCollections.observableArrayList();
    private ObservableList<EncomendaCliente> encomendasList = FXCollections.observableArrayList();
    private ObservableList<TarefaDTO> tarefasList = FXCollections.observableArrayList();
    private ObservableList<EtapaDTO> etapasList = FXCollections.observableArrayList();

    private EncomendaCliente encomendaSelecionada;
    private TarefaDTO tarefaSelecionada;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureComboBox();
        configureEncomendasTable();
        configureTarefasTable();
        configureEtapasTable();
        loadFuncionariosData();

        funcionarioComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadEncomendasData(newVal.getId());
            }
        });

        tarefasTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tarefaSelecionada = newVal;
                loadEtapasData(newVal.getId());
                etapasContainer.setVisible(true);
            } else {
                etapasContainer.setVisible(false);
            }
        });
    }

    private void configureComboBox() {
        funcionarioComboBox.setConverter(new StringConverter<FuncionarioProducaoDTO>() {
            @Override
            public String toString(FuncionarioProducaoDTO funcionario) {
                return funcionario == null ? "" : funcionario.getNome();
            }

            @Override
            public FuncionarioProducaoDTO fromString(String string) {
                return null;
            }
        });
    }

    private void configureEncomendasTable() {
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClienteNome()));
        colDataEncomenda.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataEncomenda()));
        colPrecoTotal.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValorTotal()));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstadoNome()));

        colDataEncomenda.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        colPrecoTotal.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f€", item));
                }
            }
        });

        configureEncomendasAcoesColumn();

        encomendasTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                encomendaSelecionada = newVal;
                loadTarefasData(newVal.getId());
                tarefasContainer.setVisible(true);
            } else {
                tarefasContainer.setVisible(false);
            }
        });

        encomendasTableView.setItems(encomendasList);
    }

    private void configureEncomendasAcoesColumn() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnVerTarefas = new Button("Ver Tarefas");
            private final Button btnNovaTarefa = new Button("Nova Tarefa");
            private final HBox pane = new HBox(5, btnVerTarefas, btnNovaTarefa);

            {
                btnVerTarefas.getStyleClass().add("btn-action");
                btnNovaTarefa.getStyleClass().addAll("btn-action", "btn-edit");

                btnVerTarefas.setOnAction(event -> {
                    EncomendaCliente encomenda = getTableView().getItems().get(getIndex());
                    loadTarefasData(encomenda.getId());
                    encomendaSelecionada = encomenda;
                    tarefasContainer.setVisible(true);
                });

                btnNovaTarefa.setOnAction(event -> {
                    EncomendaCliente encomenda = getTableView().getItems().get(getIndex());
                    criarNovaTarefa(encomenda.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void configureTarefasTable() {
        colTarefaId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colEncomendaId.setCellValueFactory(cellData -> new SimpleStringProperty("Encomenda #" + cellData.getValue().getEncomendaId()));
        colDescricao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricaoNome()));
        colDataInicio.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataInicio()));
        colDataFim.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataFim()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));

        colDataInicio.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        colDataFim.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("-");
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    getStyleClass().removeAll("status-pendente", "status-em-progresso", "status-concluido");
                    switch (item) {
                        case "PENDENTE":
                            getStyleClass().add("status-pendente");
                            break;
                        case "EM_PROGRESSO":
                            getStyleClass().add("status-em-progresso");
                            break;
                        case "CONCLUIDO":
                            getStyleClass().add("status-concluido");
                            break;
                    }
                }
            }
        });

        configureTarefasAcoesColumn();
        tarefasTableView.setItems(tarefasList);
    }

    private void configureTarefasAcoesColumn() {
        colTarefaAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEtapa = new Button("Nova Etapa");
            private final HBox pane = new HBox(5, btnEditar, btnEtapa);

            {
                btnEditar.getStyleClass().addAll("btn-action", "btn-edit");
                btnEtapa.getStyleClass().add("btn-action");

                btnEditar.setOnAction(event -> {
                    TarefaDTO tarefa = getTableView().getItems().get(getIndex());
                    editarTarefa(tarefa);
                });

                btnEtapa.setOnAction(event -> {
                    TarefaDTO tarefa = getTableView().getItems().get(getIndex());
                    criarNovaEtapa(tarefa.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }
                setGraphic(pane);
            }
        });
    }

    private void configureEtapasTable() {
        colEtapaId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colTarefaRef.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTarefaId()));
        colEtapaDescricao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
        colEtapaDataInicio.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataInicio()));
        colEtapaDataFim.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataFim()));

        colEtapaDataInicio.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        colEtapaDataFim.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("-");
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });



        configureEtapasAcoesColumn();
        etapasTableView.setItems(etapasList);
    }

    private void configureEtapasAcoesColumn() {
        colEtapaAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final HBox pane = new HBox(5, btnEditar);

            {
                btnEditar.getStyleClass().addAll("btn-action", "btn-edit");

                btnEditar.setOnAction(event -> {
                    EtapaDTO etapa = getTableView().getItems().get(getIndex());
                    editarEtapa(etapa);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                EtapaDTO etapa = getTableView().getItems().get(getIndex());
                setGraphic(pane);
            }
        });
    }

    private void loadFuncionariosData() {
        try {
            List<FuncionarioProducaoDTO> funcionarios = utilizadorService.getFuncionariosPorCargo(3);
            funcionariosList.setAll(funcionarios);
            funcionarioComboBox.setItems(funcionariosList);
        } catch (Exception e) {
            DialogUtils.showErrorDialog("Erro", "Não foi possível carregar os funcionários",
                    "Ocorreu um erro ao tentar carregar a lista de funcionários: " + e.getMessage());
        }
    }

    private void loadEncomendasData(Integer funcionarioId) {
        try {
            List<EncomendaCliente> encomendas = producaoService.getEncomendasDoFuncionario(funcionarioId);
            encomendasList.setAll(encomendas);
            encomendaInfoContainer.setVisible(!encomendas.isEmpty());
            tarefasContainer.setVisible(false);
            etapasContainer.setVisible(false);
        } catch (Exception e) {
            DialogUtils.showErrorDialog("Erro", "Não foi possível carregar as encomendas",
                    "Ocorreu um erro ao tentar carregar a lista de encomendas: " + e.getMessage());
        }
    }

    private void loadTarefasData(Integer encomendaId) {
        try {
            List<TarefaDTO> tarefas = producaoService.getTarefasDaEncomenda(encomendaId);
            tarefasList.setAll(tarefas);
            tarefasContainer.setVisible(true);
            etapasContainer.setVisible(false);
        } catch (Exception e) {
            DialogUtils.showErrorDialog("Erro", "Não foi possível carregar as tarefas",
                    "Ocorreu um erro ao tentar carregar a lista de tarefas: " + e.getMessage());
        }
    }

    private void loadEtapasData(Integer tarefaId) {
        try {
            List<EtapaDTO> etapas = producaoService.getEtapasDaTarefa(tarefaId);
            etapasList.setAll(etapas);
        } catch (Exception e) {
            DialogUtils.showErrorDialog("Erro", "Não foi possível carregar as etapas",
                    "Ocorreu um erro ao tentar carregar a lista de etapas: " + e.getMessage());
        }
    }

    @FXML
    private void onFuncionarioSelected(ActionEvent event) {
        FuncionarioProducaoDTO funcionario = funcionarioComboBox.getValue();
        if (funcionario != null) {
            try {
                // Carregar tarefas diretamente pelo ID do funcionário
                List<TarefaDTO> tarefas = producaoService.getTarefasPorFuncionario(funcionario.getId());
                tarefasList.setAll(tarefas);

                // Mostrar a seção de tarefas
                tarefasContainer.setVisible(!tarefas.isEmpty());

                if (!tarefas.isEmpty()) {
                    // Selecionar automaticamente a primeira tarefa
                    tarefasTableView.getSelectionModel().selectFirst();
                    TarefaDTO primeiraTarefa = tarefas.get(0);
                    tarefaSelecionada = primeiraTarefa;

                    // Carregar etapas da primeira tarefa
                    loadEtapasData(primeiraTarefa.getId());
                    etapasContainer.setVisible(true);

                    // Obter as encomendas relacionadas às tarefas (opcional)
                    loadEncomendaDataFromTarefas(tarefas);
                } else {
                    etapasContainer.setVisible(false);
                    encomendaInfoContainer.setVisible(false);
                    DialogUtils.showInformationDialog(
                        "Sem tarefas",
                        "Nenhuma tarefa encontrada",
                        "Este funcionário não possui tarefas associadas."
                    );
                }
            } catch (Exception e) {
                DialogUtils.showErrorDialog(
                    "Erro",
                    "Não foi possível carregar os dados",
                    "Ocorreu um erro ao tentar carregar as tarefas do funcionário: " + e.getMessage()
                );
                e.printStackTrace();
            }
        }
    }

    /**
     * Carrega as informações das encomendas relacionadas às tarefas
     * @param tarefas Lista de tarefas do funcionário
     */
    private void loadEncomendaDataFromTarefas(List<TarefaDTO> tarefas) {
        try {
            // Extrair IDs únicos de encomendas das tarefas
            Set<Integer> encomendaIds = tarefas.stream()
                .map(TarefaDTO::getEncomendaId)
                .collect(Collectors.toSet());

            // Para cada ID de encomenda, buscar os detalhes e adicionar à lista
            List<EncomendaCliente> encomendas = new ArrayList<>();
            for (Integer encomendaId : encomendaIds) {
                // Aqui você precisaria de um método para buscar os detalhes da encomenda pelo ID
                // Por enquanto, vamos criar objetos temporários com dados mínimos
                EncomendaCliente encomenda = new EncomendaCliente();
                encomenda.setId(encomendaId);

                String nomeCliente = encomenda.getClienteNome();
                encomenda.setClienteNome(nomeCliente);

                // Definir valores padrão para campos obrigatórios
                encomenda.setDataEncomenda(LocalDate.now());
                encomenda.setValorTotal(new BigDecimal("0.00"));

                // Criar um estado básico para a encomenda
                EstadoEncomenda estado = new EstadoEncomenda();
                estado.setId(1); // ID padrão
                estado.setNome("Em Processamento"); // Nome padrão
                encomenda.setEstado(estado);

                encomendas.add(encomenda);
            }

            encomendasList.setAll(encomendas);
            encomendaInfoContainer.setVisible(!encomendas.isEmpty());

            // Se houver encomendas, selecionar a primeira
            if (!encomendas.isEmpty()) {
                encomendasTableView.getSelectionModel().selectFirst();
                encomendaSelecionada = encomendas.get(0);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados das encomendas: " + e.getMessage());
            // Não exibir erro para não interromper o fluxo principal
        }
    }


    @FXML
    private void onNovaTarefaClicked(ActionEvent event) {
        if (funcionarioComboBox.getValue() == null) {
            DialogUtils.showWarningDialog("Atenção", "Selecione um funcionário",
                    "Por favor, selecione um funcionário antes de criar uma nova tarefa.");
            return;
        }

        if (encomendaSelecionada != null) {
            criarNovaTarefa(encomendaSelecionada.getId());
        } else {
            DialogUtils.showWarningDialog("Atenção", "Selecione uma encomenda",
                    "Por favor, selecione uma encomenda antes de criar uma nova tarefa.");
        }
    }

    private void criarNovaTarefa(Integer encomendaId) {
        Dialog<TarefaDTO> dialog = new Dialog<>();
        dialog.setTitle("Nova Tarefa");
        dialog.setHeaderText("Criar nova tarefa para a encomenda #" + encomendaId);

        ButtonType buttonTypeOk = new ButtonType("Criar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        TextField descricaoField = new TextField();
        descricaoField.setPromptText("Descrição");

        DatePicker dataInicioDatePicker = new DatePicker(LocalDate.now());

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Descrição:"), descricaoField,
            new Label("Data de Início:"), dataInicioDatePicker
        ));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (descricaoField.getText().isEmpty()) {
                    DialogUtils.showWarningDialog("Campos obrigatórios", "Preencha todos os campos",
                            "A descrição da tarefa é obrigatória.");
                    return null;
                }

                TarefaDTO novaTarefa = new TarefaDTO();
                novaTarefa.setDescricaoNome(descricaoField.getText());
                novaTarefa.setEncomendaId(encomendaId);
                novaTarefa.setDataInicio(dataInicioDatePicker.getValue());
                novaTarefa.setEstado("PENDENTE");
                novaTarefa.setFuncionarioId(funcionarioComboBox.getValue().getId());

                return novaTarefa;
            }
            return null;
        });

        Optional<TarefaDTO> result = dialog.showAndWait();
        result.ifPresent(novaTarefa -> {
            try {
                TarefaDTO tarefaCriada = producaoService.criarTarefa(novaTarefa);
                tarefasList.add(tarefaCriada);
                DialogUtils.showInformationDialog("Sucesso", "Tarefa criada",
                        "A tarefa foi criada com sucesso.");
            } catch (Exception e) {
                DialogUtils.showErrorDialog("Erro", "Não foi possível criar a tarefa",
                        "Ocorreu um erro ao tentar criar a tarefa: " + e.getMessage());
            }
        });
    }

    private void editarTarefa(TarefaDTO tarefa) {
        Dialog<TarefaDTO> dialog = new Dialog<>();
        dialog.setTitle("Editar Tarefa");
        dialog.setHeaderText("Editar tarefa #" + tarefa.getId());

        ButtonType buttonTypeOk = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        TextField descricaoField = new TextField(tarefa.getDescricaoNome());
        DatePicker dataInicioDatePicker = new DatePicker(tarefa.getDataInicio());
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("PENDENTE", "EM_PROGRESSO", "CONCLUIDO");
        statusComboBox.setValue(tarefa.getEstado());

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Descrição:"), descricaoField,
            new Label("Data de Início:"), dataInicioDatePicker,
            new Label("Status:"), statusComboBox
        ));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (descricaoField.getText().isEmpty()) {
                    DialogUtils.showWarningDialog("Campos obrigatórios", "Preencha todos os campos",
                            "A descrição da tarefa é obrigatória.");
                    return null;
                }

                tarefa.setDescricaoNome(descricaoField.getText());
                tarefa.setDataInicio(dataInicioDatePicker.getValue());
                tarefa.setEstado(statusComboBox.getValue());

                if ("CONCLUIDO".equals(statusComboBox.getValue())) {
                    tarefa.setDataFim(LocalDate.now());
                }

                return tarefa;
            }
            return null;
        });

        Optional<TarefaDTO> result = dialog.showAndWait();
        result.ifPresent(tarefaAtualizada -> {
            try {
                TarefaDTO tarefaAtualizadaResposta = producaoService.atualizarTarefa(tarefaAtualizada);
                int index = tarefasList.indexOf(tarefa);
                if (index >= 0) {
                    tarefasList.set(index, tarefaAtualizadaResposta);
                }
                DialogUtils.showInformationDialog("Sucesso", "Tarefa atualizada",
                        "A tarefa foi atualizada com sucesso.");
            } catch (Exception e) {
                DialogUtils.showErrorDialog("Erro", "Não foi possível atualizar a tarefa",
                        "Ocorreu um erro ao tentar atualizar a tarefa: " + e.getMessage());
            }
        });
    }

    private void criarNovaEtapa(Integer tarefaId) {
        Dialog<EtapaDTO> dialog = new Dialog<>();
        dialog.setTitle("Nova Etapa");
        dialog.setHeaderText("Criar nova etapa para a tarefa #" + tarefaId);

        ButtonType buttonTypeOk = new ButtonType("Criar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        TextField descricaoField = new TextField();
        descricaoField.setPromptText("Descrição");

        DatePicker dataInicioDatePicker = new DatePicker(LocalDate.now());

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Descrição:"), descricaoField,
            new Label("Data de Início:"), dataInicioDatePicker
        ));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (descricaoField.getText().isEmpty()) {
                    DialogUtils.showWarningDialog("Campos obrigatórios", "Preencha todos os campos",
                            "A descrição da etapa é obrigatória.");
                    return null;
                }

                EtapaDTO novaEtapa = new EtapaDTO();
                novaEtapa.setDescricao(descricaoField.getText());
                novaEtapa.setTarefaId(tarefaId);
                novaEtapa.setDataInicio(dataInicioDatePicker.getValue());

                return novaEtapa;
            }
            return null;
        });

        Optional<EtapaDTO> result = dialog.showAndWait();
        result.ifPresent(novaEtapa -> {
            try {
                EtapaDTO etapaCriada = producaoService.criarEtapa(novaEtapa);
                etapasList.add(etapaCriada);
                DialogUtils.showInformationDialog("Sucesso", "Etapa criada",
                        "A etapa foi criada com sucesso.");
            } catch (Exception e) {
                DialogUtils.showErrorDialog("Erro", "Não foi possível criar a etapa",
                        "Ocorreu um erro ao tentar criar a etapa: " + e.getMessage());
            }
        });
    }

    private void editarEtapa(EtapaDTO etapa) {
        Dialog<EtapaDTO> dialog = new Dialog<>();
        dialog.setTitle("Editar Etapa");
        dialog.setHeaderText("Editar etapa #" + etapa.getId());

        ButtonType buttonTypeOk = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        TextField descricaoField = new TextField(etapa.getDescricao());
        DatePicker dataInicioDatePicker = new DatePicker(etapa.getDataInicio());
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("PENDENTE", "EM_PROGRESSO", "CONCLUIDO");

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Descrição:"), descricaoField,
            new Label("Data de Início:"), dataInicioDatePicker,
            new Label("Status:"), statusComboBox
        ));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (descricaoField.getText().isEmpty()) {
                    DialogUtils.showWarningDialog("Campos obrigatórios", "Preencha todos os campos",
                            "A descrição da etapa é obrigatória.");
                    return null;
                }

                etapa.setDescricao(descricaoField.getText());
                etapa.setDataInicio(dataInicioDatePicker.getValue());

                if ("CONCLUIDO".equals(statusComboBox.getValue())) {
                    etapa.setDataFim(LocalDate.now());
                }

                return etapa;
            }
            return null;
        });

        Optional<EtapaDTO> result = dialog.showAndWait();
        result.ifPresent(etapaAtualizada -> {
            try {
                EtapaDTO etapaAtualizadaResposta = producaoService.atualizarEtapa(etapaAtualizada);
                int index = etapasList.indexOf(etapa);
                if (index >= 0) {
                    etapasList.set(index, etapaAtualizadaResposta);
                }
                DialogUtils.showInformationDialog("Sucesso", "Etapa atualizada",
                        "A etapa foi atualizada com sucesso.");
            } catch (Exception e) {
                DialogUtils.showErrorDialog("Erro", "Não foi possível atualizar a etapa",
                        "Ocorreu um erro ao tentar atualizar a etapa: " + e.getMessage());
            }
        });
    }

    private void concluirEtapa(EtapaDTO etapa) {
        boolean confirm = DialogUtils.showConfirmationDialog("Concluir Etapa",
                "Tem certeza que deseja concluir esta etapa?",
                "Esta ação não pode ser desfeita.");

        if (confirm) {
            try {
                etapa.setDataFim(LocalDate.now());

                EtapaDTO etapaAtualizadaResposta = producaoService.atualizarEtapa(etapa);
                int index = etapasList.indexOf(etapa);
                if (index >= 0) {
                    etapasList.set(index, etapaAtualizadaResposta);
                }

                DialogUtils.showInformationDialog("Sucesso", "Etapa concluída",
                        "A etapa foi marcada como concluída com sucesso.");
            } catch (Exception e) {
                DialogUtils.showErrorDialog("Erro", "Não foi possível concluir a etapa",
                        "Ocorreu um erro ao tentar concluir a etapa: " + e.getMessage());
            }
        }
    }
}
