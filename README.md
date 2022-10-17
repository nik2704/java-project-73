### Hexlet tests and linter taskStatus:
[![Actions Status](https://github.com/nik2704/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/nik2704/java-project-73/actions)

![Java CI](https://github.com/nik2704/java-project-73/actions/workflows/blank.yml/badge.svg)

<a href="https://codeclimate.com/github/nik2704/java-project-lvl3/maintainability"><img src="https://api.codeclimate.com/v1/badges/afb66a0d8d00ae36edc0/maintainability" /></a>

<a href="https://codeclimate.com/github/nik2704/java-project-lvl3/test_coverage"><img src="https://api.codeclimate.com/v1/badges/afb66a0d8d00ae36edc0/test_coverage" /></a>

# **Менеджер задач**

#### Для просмотра работы приложения (Heroku) можно перейти по [ссылке](https://stark-oasis-25503.herokuapp.com/).

#### Задокументированные методы API (контроллеров для пользователей, задач и меток) можно изучить [здесь](https://stark-oasis-25503.herokuapp.com/swagger-ui.html).

## Описание

С помощью Менеджера Задач можно регистрировать, обновлять и удалять задания, которые хранятся в системе в структурированном виде.

### Структура задания:
* статус (ступень жизненного цикла задания),
* название,
* описание,
* исполнитель,
* автор,
* метки (может ассоциироваться с несколькими метками для удобства фильтрации и отражения дополнительной информации о сути задачи).

Для начала работы необходимо зарегистрироваться (для входа в систему, а также можно создать в системе пользователей, от имени которых будет выполняться работ с Менеджером задач).

Для удобства работы со списком задач предусмотрен гибкий механизм фильтрации.

