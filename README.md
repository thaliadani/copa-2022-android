# Copa 2022

App que mostra informações sobre os jogos da Copa do Mundo de 2022.

## Funcionalidades

* Listagem de jogos
* Notificações de jogos
* Filtragem de jogos

## Tecnologias

* Kotlin
* Jetpack Compose
* Hilt
* Retrofit
* Room
* WorkManager

## Arquitetura

O projeto é dividido nos seguintes módulos:

* `:app`: Contém a UI e a lógica de apresentação.
* `:domain`: Contém as regras de negócio do app.
* `:data:data`: Contém a implementação do repositório.
* `:data:remote`: Contém a implementação do data source remoto.
* `:data:local`: Contém a implementação do data source local.
* `:notification-scheduler`: Contém a implementação do agendamento de notificações.
