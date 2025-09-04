# DocCraft - Web Document Template Processor

[![Java](https://img.shields.io/badge/Java-17-orange.svg?logo=java)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-6DB33F.svg?logo=spring)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0.41-4479A1.svg?logo=mysql)](https://www.mysql.com/)
[![Apache POI](https://img.shields.io/badge/Apache_POI-5.2.5-D22128.svg?logo=apache)](https://poi.apache.org/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1.3-005F0F.svg?logo=thymeleaf)](https://www.thymeleaf.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5-7952B3.svg?logo=bootstrap)](https://getbootstrap.com/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162.svg?logo=junit5)](https://junit.org/junit5/)

## Описание

DocCraft - это веб-приложение, позволяющее автоматизировать процесс создания документов на основе шаблонов.
Приложение поддерживает работу с документами форматов .doc и .docx, позволяет заполнять теги в шаблонах данными,
генерировать документы для нескольких авторов и конвертировать результат в PDF.
Это веб-версия десктопного приложения [Template Process](https://github.com/stifell/template-process).

## Основные возможности

- 📄 Загрузка и управление пакетами шаблонов документов
- 🔍 Автоматическое определение тегов в шаблонах
- 🖊️ Заполнение шаблонов данными через веб-интерфейс
- 👥 Поддержка документов с несколькими авторами
- 📦 Генерация документов в форматах .doc, .docx и PDF
- 🗜️ Экспорт сгенерированных документов в ZIP-архив
- 📜 История генерации с возможностью просмотра предыдущих результатов
- 👤 Пользовательская аутентификация и авторизация
- 🌐 Административная панель для управления шаблонами

## Технологии

- **Java 17** - основной язык программирования
- **Spring Boot 3.4.5** - фреймворк для создания веб-приложения
- **Spring Data JPA** - работа с базой данных
- **Spring Security** - аутентификация и авторизация
- **MySQL** - база данных
- **Hibernate** - ORM
- **Thymeleaf** - шаблонизатор для веб-страниц
- **Apache POI** - обработка документов форматов DOC/DOCX
- **LibreOffice** (через JODConverter) - конвертация в PDF
- **Bootstrap 5** - фронтенд-стилизация
- **Commons IO / Commons CSV** – для работы с файлами и CSV
- **Zip4j** – для упаковки/распаковки ZIP-архивов
- **JUnit 5 + Spring Security Test** – тестирование

## Отличия от десктопной версии

Веб-версия DocCraft предоставляет все функции оригинального десктопного приложения [Template Process](https://github.com/stifell/template-process) с дополнительными преимуществами:

- Доступ к системе из любого браузера без установки
- Централизованное хранение шаблонов и истории генерации
- Улучшенный интерфейс с возможностью предпросмотра тегов
- Интеграция с системой аутентификации
- Управление пользователями и ролями через административную панель

## Лицензия

Этот проект лицензирован в соответствии с лицензией MIT. Подробнее см. в файле [LICENSE](https://gitgihub.com/stifell/template-process-web/blob/master/LICENSE).
