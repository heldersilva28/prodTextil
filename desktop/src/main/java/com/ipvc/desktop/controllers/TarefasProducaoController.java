package com.ipvc.desktop.controllers;

import com.ipvc.desktop.models.EncomendaCliente;
import com.ipvc.desktop.models.EstadoEncomenda;
import com.ipvc.desktop.models.EtapaDTO;
import com.ipvc.desktop.models.FuncionarioProducaoDTO;
import com.ipvc.desktop.models.TarefaDTO;
import com.ipvc.desktop.models.TipoEtapa;
import com.ipvc.desktop.models.TipoEvento;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.ZoneId;

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
    private TableColumn<TarefaDTO, Instant> colDataInicio;

    @FXML
    private TableColumn<TarefaDTO, Instant> colDataFim;

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
    private TableColumn<EtapaDTO, Instant> colEtapaDataInicio;

    @FXML
    private TableColumn<EtapaDTO, Instant> colEtapaDataFim;


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
            private final Button btnNovaTarefa = new Button("Nova Tarefa");
            private final HBox pane = new HBox(5, btnNovaTarefa);

            {
                btnNovaTarefa.getStyleClass().addAll("btn-action", "btn-edit");

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
            protected void updateItem(Instant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Convertendo Instant para LocalDate antes de formatar
                    LocalDate localDate = item.atZone(ZoneId.systemDefault()).toLocalDate();
                    setText(dateFormatter.format(localDate));
                }
            }
        });

        colDataFim.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Instant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("-");
                } else {
                    // Convertendo Instant para LocalDate antes de formatar
                    LocalDate localDate = item.atZone(ZoneId.systemDefault()).toLocalDate();
                    setText(dateFormatter.format(localDate));
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
            private final Button btnEtapa = new Button("Nova Etapa");
            private final HBox pane = new HBox(5, btnEtapa);

            {
                btnEtapa.getStyleClass().add("btn-action");

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

                TarefaDTO tarefa = getTableView().getItems().get(getIndex());

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
            protected void updateItem(Instant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    LocalDate localDate = item.atZone(ZoneId.systemDefault()).toLocalDate();
                    setText(dateFormatter.format(localDate));
                }
            }
        });

        colEtapaDataFim.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Instant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("-");
                } else {
                    LocalDate localDate = item.atZone(ZoneId.systemDefault()).toLocalDate();
                    setText(dateFormatter.format(localDate));
                }
            }
        });



        configureEtapasAcoesColumn();
        etapasTableView.setItems(etapasList);
    }

    private void configureEtapasAcoesColumn() {
        colEtapaAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnConcluir = new Button("Concluir");
            private final HBox pane = new HBox(5, btnConcluir);

            {
                btnConcluir.getStyleClass().addAll("btn-action", "btn-success");

                btnConcluir.setOnAction(event -> {
                    EtapaDTO etapa = getTableView().getItems().get(getIndex());
                    concluirEtapa(etapa);
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

                // Desabilita o botão de concluir se a etapa já estiver concluída
                btnConcluir.setDisable(etapa.getDataFim() != null);

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
     * Carrega as informaç��es das encomendas relacionadas às tarefas
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

    @FXML
    private void onNovaEncomendaClicked(ActionEvent event) {
        if (funcionarioComboBox.getValue() == null) {
            DialogUtils.showWarningDialog("Atenção", "Selecione um funcionário",
                    "Por favor, selecione um funcionário antes de criar uma nova tarefa.");
            return;
        }

        try {
            // Buscar encomendas sem tarefas do backend
            List<EncomendaCliente> encomendasSemTarefas = producaoService.getEncomendasSemTarefas();

            if (encomendasSemTarefas.isEmpty()) {
                DialogUtils.showInformationDialog("Informação", "Sem encomendas disponíveis",
                        "Não existem encomendas sem tarefas de produção associadas.");
                return;
            }

            // Criar diálogo personalizado
            Dialog<EncomendaCliente> dialog = new Dialog<>();
            dialog.setTitle("Nova Tarefa");
            dialog.setHeaderText("Selecione uma encomenda para criar uma nova tarefa");

            // Configurar botões
            ButtonType criarTarefaButtonType = new ButtonType("Criar Tarefa", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(criarTarefaButtonType, ButtonType.CANCEL);

            // Criar tabela para mostrar as encomendas
            TableView<EncomendaCliente> tabelaEncomendas = new TableView<>();
            tabelaEncomendas.setPrefHeight(300);
            tabelaEncomendas.setPrefWidth(600);
            tabelaEncomendas.getStyleClass().add("tabela-encomendas");

            // Configurar colunas da tabela (mesmo estilo da gestão de clientes)
            TableColumn<EncomendaCliente, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
            colId.setPrefWidth(50);

            TableColumn<EncomendaCliente, String> colCliente = new TableColumn<>("Cliente");
            colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClienteNome()));
            colCliente.setPrefWidth(150);

            TableColumn<EncomendaCliente, LocalDate> colData = new TableColumn<>("Data");
            colData.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataEncomenda()));
            colData.setCellFactory(col -> new TableCell<>() {
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
            colData.setPrefWidth(100);

            TableColumn<EncomendaCliente, String> colEstado = new TableColumn<>("Estado");
            colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstadoNome()));
            colEstado.setPrefWidth(100);

            TableColumn<EncomendaCliente, BigDecimal> colValor = new TableColumn<>("Valor Total");
            colValor.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValorTotal()));
            colValor.setCellFactory(col -> new TableCell<>() {
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
            colValor.setPrefWidth(100);

            // Adicionar colunas à tabela
            tabelaEncomendas.getColumns().addAll(colId, colCliente, colData, colEstado, colValor);

            // Definir os dados na tabela
            tabelaEncomendas.setItems(FXCollections.observableArrayList(encomendasSemTarefas));

            // Adicionar a tabela ao conteúdo do diálogo
            VBox content = new VBox(10, tabelaEncomendas);
            content.setPadding(new Insets(20));
            dialog.getDialogPane().setContent(content);

            // Desabilitar o botão de criar tarefa até que uma encomenda seja selecionada
            Node criarButton = dialog.getDialogPane().lookupButton(criarTarefaButtonType);
            criarButton.setDisable(true);

            // Habilitar botão quando uma encomenda é selecionada
            tabelaEncomendas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                criarButton.setDisable(newVal == null);
            });

            // Configurar o conversor de resultado
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == criarTarefaButtonType) {
                    return tabelaEncomendas.getSelectionModel().getSelectedItem();
                }
                return null;
            });

            // Mostrar o diálogo e processar o resultado
            Optional<EncomendaCliente> resultado = dialog.showAndWait();
            resultado.ifPresent(encomenda -> {
                TarefaDTO novaTarefa = new TarefaDTO();
                novaTarefa.setEncomendaId(encomenda.getId());
                novaTarefa.setDescricao(7); // Descrição padrão: 7
                novaTarefa.setFuncionarioId(funcionarioComboBox.getValue().getId());
                novaTarefa.setDataInicio(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
                novaTarefa.setEstado("Preparação"); // Estado padrão: Preparação

                try {
                    TarefaDTO tarefaCriada = producaoService.criarTarefa(novaTarefa);
                    // Após criar a tarefa com sucesso, adiciona à lista e mostra na interface
                    tarefasList.add(tarefaCriada);
                    // Atualiza a exibição
                    loadEncomendasData(funcionarioComboBox.getValue().getId());
                    DialogUtils.showInformationDialog("Sucesso", "Tarefa criada",
                            "A tarefa de preparação foi criada com sucesso para a encomenda #" + encomenda.getId());
                } catch (Exception e) {
                    DialogUtils.showErrorDialog("Erro", "Não foi possível criar a tarefa",
                            "Ocorreu um erro ao tentar criar a tarefa: " + e.getMessage());
                    System.out.println("Erro ao criar tarefa: " + e.getMessage());
                }
            });

        } catch (Exception e) {
            DialogUtils.showErrorDialog("Erro", "Não foi possível carregar as encomendas",
                    "Ocorreu um erro ao tentar carregar as encomendas sem tarefas: " + e.getMessage());
        }
    }

    private void criarNovaTarefa(Integer encomendaId) {
        Dialog<TarefaDTO> dialog = new Dialog<>();
        dialog.setTitle("Nova Tarefa");
        dialog.setHeaderText("Criar nova tarefa para a encomenda #" + encomendaId);

        ButtonType buttonTypeOk = new ButtonType("Criar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        // ComboBox para selecionar o tipo de evento/tarefa
        ComboBox<TipoEvento> tipoEventoComboBox = new ComboBox<>();
        tipoEventoComboBox.setPromptText("Selecione o tipo de tarefa");

        // ComboBox para estado
        ComboBox<String> estadoComboBox = new ComboBox<>();
        estadoComboBox.getItems().addAll("PENDENTE", "EM_PROGRESSO", "CONCLUIDO");
        estadoComboBox.setValue("PENDENTE"); // Valor padrão

        // Carregar os tipos de eventos da API
        try {
            List<TipoEvento> tiposEventos = producaoService.getTiposEventos();
            tipoEventoComboBox.setItems(FXCollections.observableArrayList(tiposEventos));
        } catch (Exception e) {
            DialogUtils.showErrorDialog("Erro", "Não foi possível carregar os tipos de tarefas",
                    "Ocorreu um erro ao tentar carregar os tipos de tarefas: " + e.getMessage());
        }

        DatePicker dataInicioDatePicker = new DatePicker(LocalDate.now());

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Tipo de Tarefa:"), tipoEventoComboBox,
            new Label("Data de Início:"), dataInicioDatePicker,
            new Label("Estado:"), estadoComboBox
        ));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                TipoEvento tipoEvento = tipoEventoComboBox.getValue();
                if (tipoEvento == null) {
                    DialogUtils.showWarningDialog("Campos obrigatórios", "Selecione um tipo de tarefa",
                            "O tipo de tarefa é obrigatório.");
                    return null;
                }

                TarefaDTO novaTarefa = new TarefaDTO();
                novaTarefa.setDescricao(tipoEvento.getId()); // ID do tipo de evento/tarefa
                novaTarefa.setDescricaoNome(tipoEvento.getNome()); // Nome para exibição
                novaTarefa.setEncomendaId(encomendaId);

                // Converte LocalDate para Instant
                novaTarefa.setDataInicio(dataInicioDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                novaTarefa.setEstado(estadoComboBox.getValue());

                // Se o estado for CONCLUIDO, definir data de fim
                if ("CONCLUIDO".equals(estadoComboBox.getValue())) {
                    novaTarefa.setDataFim(Instant.now());
                }

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
                e.printStackTrace();
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
        DatePicker dataInicioDatePicker = new DatePicker();
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
                tarefa.setDataInicio(Instant.from(dataInicioDatePicker.getValue()));
                tarefa.setEstado(statusComboBox.getValue());

                if ("CONCLUIDO".equals(statusComboBox.getValue())) {
                    tarefa.setDataFim(Instant.from(LocalDate.now()));
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

        // ComboBox para selecionar o tipo de etapa
        ComboBox<TipoEtapa> tipoEtapaComboBox = new ComboBox<>();
        tipoEtapaComboBox.setPromptText("Selecione o tipo de etapa");

        // Carregar os tipos de etapas da API
        try {
            List<TipoEtapa> tiposEtapas = producaoService.getTiposEtapas();
            tipoEtapaComboBox.setItems(FXCollections.observableArrayList(tiposEtapas));
        } catch (Exception e) {
            DialogUtils.showErrorDialog("Erro", "Não foi possível carregar os tipos de etapas",
                    "Ocorreu um erro ao tentar carregar os tipos de etapas: " + e.getMessage());
        }

        DatePicker dataInicioDatePicker = new DatePicker(LocalDate.now());

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Tipo de Etapa:"), tipoEtapaComboBox,
            new Label("Data de Início:"), dataInicioDatePicker
        ));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                TipoEtapa tipoEtapa = tipoEtapaComboBox.getValue();
                if (tipoEtapa == null) {
                    DialogUtils.showWarningDialog("Campos obrigatórios", "Selecione um tipo de etapa",
                            "O tipo de etapa é obrigatório.");
                    return null;
                }

                EtapaDTO novaEtapa = new EtapaDTO();
                novaEtapa.setTarefaId(tarefaId);
                novaEtapa.setTipoEtapaId(tipoEtapa.getId());
                novaEtapa.setDescricao(tipoEtapa.getDescricao()); // Mantém a descrição para exibição

                // Converte LocalDate para Instant
                Instant dataInicio = dataInicioDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant();
                novaEtapa.setDataInicio(dataInicio);

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
                e.printStackTrace();
            }
        });
    }

    private void editarEtapa(EtapaDTO etapa) {
        Dialog<EtapaDTO> dialog = new Dialog<>();
        dialog.setTitle("Editar Etapa");
        dialog.setHeaderText("Editar etapa #" + etapa.getId());

        ButtonType buttonTypeOk = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        // ComboBox para selecionar o tipo de etapa
        ComboBox<TipoEtapa> tipoEtapaComboBox = new ComboBox<>();
        tipoEtapaComboBox.setPromptText("Selecione o tipo de etapa");

        // Carregar os tipos de etapas da API
        try {
            List<TipoEtapa> tiposEtapas = producaoService.getTiposEtapas();
            tipoEtapaComboBox.setItems(FXCollections.observableArrayList(tiposEtapas));

            // Tenta selecionar o tipo de etapa atual
            if (etapa.getTipoEtapaId() != null) {
                for (TipoEtapa tipoEtapa : tiposEtapas) {
                    if (tipoEtapa.getId().equals(etapa.getTipoEtapaId())) {
                        tipoEtapaComboBox.setValue(tipoEtapa);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            DialogUtils.showErrorDialog("Erro", "Não foi possível carregar os tipos de etapas",
                    "Ocorreu um erro ao tentar carregar os tipos de etapas: " + e.getMessage());
        }

        // Converte Instant para LocalDate para o DatePicker
        LocalDate dataInicio = null;
        if (etapa.getDataInicio() != null) {
            dataInicio = etapa.getDataInicio().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        DatePicker dataInicioDatePicker = new DatePicker(dataInicio != null ? dataInicio : LocalDate.now());

        // ComboBox para status
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("PENDENTE", "EM_PROGRESSO", "CONCLUIDO");

        // Define o status com base na presença de dataFim
        if (etapa.getDataFim() != null) {
            statusComboBox.setValue("CONCLUIDO");
        } else {
            statusComboBox.setValue("EM_PROGRESSO");
        }

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Tipo de Etapa:"), tipoEtapaComboBox,
            new Label("Data de Início:"), dataInicioDatePicker,
            new Label("Status:"), statusComboBox
        ));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                TipoEtapa tipoEtapa = tipoEtapaComboBox.getValue();
                if (tipoEtapa == null) {
                    DialogUtils.showWarningDialog("Campos obrigatórios", "Selecione um tipo de etapa",
                            "O tipo de etapa é obrigatório.");
                    return null;
                }

                // Mantém o ID original
                etapa.setTipoEtapaId(tipoEtapa.getId());
                etapa.setDescricao(tipoEtapa.getDescricao()); // Atualiza a descrição para exibição

                // Converte LocalDate para Instant
                etapa.setDataInicio(dataInicioDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

                // Define a data de conclusão se o status for CONCLUIDO
                if ("CONCLUIDO".equals(statusComboBox.getValue())) {
                    etapa.setDataFim(Instant.now());
                } else {
                    etapa.setDataFim(null);
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
                e.printStackTrace();
            }
        });
    }

    private void concluirEtapa(EtapaDTO etapa) {
        boolean confirm = DialogUtils.showConfirmationDialog("Concluir Etapa",
                "Tem certeza que deseja concluir esta etapa?",
                "Esta ação não pode ser desfeita.");

        if (confirm) {
            try {
                etapa.setDataFim(Instant.now()); // Usando Instant.now() diretamente

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
                e.printStackTrace();
            }
        }
    }

    private void concluirTarefa(TarefaDTO tarefa) {
        boolean confirm = DialogUtils.showConfirmationDialog(
            "Concluir Tarefa",
            "Tem certeza que deseja concluir esta tarefa?",
            "Esta ação irá marcar a data de conclusão como o momento atual e não poderá ser desfeita."
        );

        if (confirm) {
            try {
                // Mantém os dados originais da tarefa e atualiza apenas a data de fim e o estado
                tarefa.setDataFim(Instant.now()); // Define como o instante atual
                tarefa.setEstado("CONCLUIDO");

                // Chama a API para atualizar a tarefa
                TarefaDTO tarefaAtualizadaResposta = producaoService.atualizarTarefa(tarefa);

                // Atualiza a tarefa na lista
                int index = tarefasList.indexOf(tarefa);
                if (index >= 0) {
                    tarefasList.set(index, tarefaAtualizadaResposta);
                }

                DialogUtils.showInformationDialog(
                    "Sucesso",
                    "Tarefa concluída",
                    "A tarefa #" + tarefa.getId() + " foi marcada como concluída com sucesso."
                );
            } catch (Exception e) {
                DialogUtils.showErrorDialog(
                    "Erro",
                    "Não foi possível concluir a tarefa",
                    "Ocorreu um erro ao tentar concluir a tarefa: " + e.getMessage()
                );
                e.printStackTrace();
            }
        }
    }
}
