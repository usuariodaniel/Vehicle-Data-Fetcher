package com.fipe.tabela.principal;

import com.fipe.tabela.model.DadosAutomovel;
import com.fipe.tabela.model.Modelos;
import com.fipe.tabela.model.Veiculo;
import com.fipe.tabela.service.ConsumoApi;
import com.fipe.tabela.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private final String MARCA = "/marcas";
    private final String MODELO = "/modelos";
    private final String ANO = "/anos";

    public void exibeMenu(){

    //interação com o usuario
    System.out.println("\nDigite a Opção desejada\n\nCarros\nMotos\nCaminhoes");
    var entradaUsuario = scanner.nextLine().toLowerCase();

    //declaração endereco api
    String endereco = ENDERECO + entradaUsuario + MARCA;

    //transformando em json
    String json = consumo.obterDados(endereco);

    //transformando em uma lista com as marcas
    var marcas = conversor.obterLista(json, DadosAutomovel.class);
    marcas.stream()
            .sorted(Comparator.comparing(DadosAutomovel::codigo))
            .forEach(System.out::println);

    //filtrando pelo código da marca
        System.out.println("\nInforme o Código da Marca que deseja consultar");
        var codigoMarca = scanner.nextLine();

        endereco = endereco + "/" + codigoMarca + MODELO;
        json = consumo.obterDados(endereco);

        var modelosLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos da Marca");
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(DadosAutomovel::codigo))
                .forEach(System.out::println);

    //Filtrando os carros
        System.out.println("\nDigite o nome do carro desejado");
        var nomeCarro = scanner.nextLine();

        List<DadosAutomovel> modelosFiltrados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeCarro.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("\nModelos Filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("\nDigite o código do modelo");
        var codigoModelo = scanner.nextLine();

        endereco = endereco + "/" + codigoModelo + ANO;
        json = consumo.obterDados(endereco);
        List<DadosAutomovel> anos = conversor.obterLista(json, DadosAutomovel.class);

        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAno = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAno);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os Veículos Filtrados");
        veiculos.forEach(System.out::println);

    }
}
