### Olá, tudo bem? Espero que sim!! 😁

Ao lado de cada requisito há um ✅ ou ⚠️ indicando que a funcionalidade foi desenvolvida totalmente ou parcialmente. Isto também serve para a parte das funcionalidades "plus".





# Teste Programador Backend Júnior/Trainee

Teste destinado aos candidatos a vaga de Programador Backend <b>Júnior</b>. 

## Descrição
Um cliente chamado Bruce Wayne nos contratou para fazer um sistema com o objetivo de catalogar os super-heróis existentes.
</br>
Parece uma missão difícil, mas, não se preocupe, o seu papel não será o de sair por aí procurando por heróis, vamos deixar isso para o Sr. Wayne...
</br>
Seu papel é desenvolver uma API com as operações básicas de cadastro de um herói e algum mago (coff, coff) do front-end fará as telas.
</br>

## Requisitos
Bom, aqui começa a explicação do que você terá que nos entregar. Leia com atenção.
</br>
Ah, não se preocupe com o setup do projeto, o Alfred (acho que ele é tipo um mordono do Sr. Wayne) começou o projeto para nós e inclusive o endpoint de cadastro de heróis já está quase pronto... É, Quase...
</br>
Quando tentamos rodar os testes do projeto identificamos um bug no cadastro, algum NullPointerException. O erro aconteceu no teste de integração.<p> Dito isso vamos deixar uma lista com as tarefas:

- [x] Resolver o bug no endpoint de cadastro de heróis; **(✅ Desenvolvido)**
- [x] Criar endpoint de busca de heróis e seus atributos por ID. ***Caso não encontre o herói o sistema deve retornar um erro 404 (Not Found)***; **(✅ Desenvolvido)**
- [x] Criar endpoint de busca de heróis e seus atributos por filtro, nesse caso o filtro será apenas o nome. ***Caso não encontre nenhum herói o sistema deve retornar um sucesso 200 com o body vazio***; **(✅ Desenvolvido)**
- [x] Criar endpoint de atualização de heróis, todos os campos poderão ser atualizados. ***Caso não encontre o herói o sistema deve retornar um erro 404 (Not Found) ** **(✅ Desenvolvido)**
- [x] Criar endpoint de exclusão de heróis. A exclusão será física, ok? (Física?! É, deleta o registro da base). ***Caso não encontre o herói o sistema deve retornar um erro 404 (Not Found)***. **(✅ Desenvolvido)**

Ah, tem algo mais! O x	 nos pediu para criar um endpoint onde ele possa selecionar dois heróis e comparar seus atributos força, agilidade, destreza e inteligência. Como resultado, o sistema deve retornar um objeto contendo os id's e a diferença dos atributos (positivo se maior, negativo se menor) de cada herói. Dá uma pensada em como vai ficar esse objeto e o caminho do endpoint, tudo bem?
<p>
Agora sim, terminamos! Se você nos entregar isso que pedimos garanto que o Sr. Wayne vai pirar!!!

## Considerações
Leia essas instruções para ganhar tempo no desenvolvimento, ok? ;)
</br>
#### Primeiro Passo
Como primeiro passo faça um ***fork*** desse projeto na sua conta do GitHub, se não tiver uma conta é só criar uma nova.
</br>
***Não iremos avaliar provas que não estejam nesse padrão, então MUITA ATENÇÃO nessa dica.***
#### Correção
Ao término da prova, envie-nos o link do github, e assim que iremos avaliar o código proposto.
#### Configurações
- OpenJDK 17 instalado;
- Maven na versão 3.8+ instalado;
- IDE pode ser o de preferência, mas gostamos bastante do IntelliJ por aqui;
- Docker e docker-compose instalados.

#### Testes
Para rodar os testes (unitários e de integração) utilize o comando a seguir:
```
mvn clean verify
```

#### Bônus
Será considerado um plus os candidatos que entregarem:
- Testes unitários e de integração das funcionalidades desenvolvidas; **(✅ Desenvolvido)**
- Bom uso dos padrões de REST;
- Um `docker-compose.yml` funcional para execução da aplicação. (Banco de Dados + API) **(⚠️ Desenvolvido, porém necessita solucionar um problema na geração de tabelas relacionado ao uuid_generate_v4() em relação ao Docker)**
