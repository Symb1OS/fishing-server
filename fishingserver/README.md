# Black Desert fishingserver

Основные функции:
- проверка наличия ключа у пользователя;
- парсинг капчи.

[Клиент для использования](https://github.com/Symb1OS/fishing)

### Requirements

docker >= 18.03.0-ce

### Building

```
docker image build -t fishingserver .

```

### Running

```
docker run -it -d -p 8080:8080 fishingserver

```