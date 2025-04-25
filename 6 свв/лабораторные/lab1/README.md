# Лабораторная работа №1

## Вариант `1`

<img alt="is-it-wrong-to-try-to-pick-up-girls-in-a-dungeon" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/is-it-wrong-to-try-to-pick-up-girls-in-a-dungeon.gif" height="350">

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

---

## Задание

**«Принципы организации ввода/вывода без операционной системы»**

| Задание ЛР | Презентация |
| --- | --- |
| [docs/lab1_task.pdf](./docs/lab1_task.pdf) | [docs/lab1_slides.pdf](./docs/lab1_slides.pdf) |

**Цель работы:** Познакомиться с принципами организации ввода/вывода без операционной системы на примере компьютерной системы на базе процессора с архитектурой RISC-V и интерфейсом OpenSBI с использованием эмулятора QEMU.

1. Реализовать функцию `putchar` вывода данных в консоль.
2. Реализовать функцию `getchar` для получения данных из консоли.
3. На базе реализованных функций `putchar` и `getchar` написать программу, позволяющую вызывать определенным вариантом функции OpenSBI посредством взаимодействия пользователя через меню.
4. Запустить программу и выполнить вызов пунктов меню, получив результаты их работы.
5. Оформить отчет по работе в электронном формате.

## Требования к отчету

1. На титульном листе должны быть приведены следующие данные:
    - Название дисциплины
    - Номер и название лабораторной работы
    - ФИО исполнителя и группа
2. Во введении указываются цели и задачи работы
3. В основной части приводится описание функций `putchar`, `getchar`, а также описывается интерфейс вызова функций OpenSBI, заданных вариантом задания
4. Приводится скриншот вывода в консоль данных при вызове каждого пункта меню.

## Варианты

> [!TIP]
> Каждый пункт меню из задания описан в документации [`riscv-sbi.pdf`](./docs/riscv-sbi.pdf) и ищется по тексту задания.

| № варианта | Пункты меню |
| --- | --- |
| 1 | 1. Get SBI specification version<br>2. Get number of counters<br>3. Get details of a counter (должно быть возможно задавать номер счетчика)<br>4. System Shutdown |
| 2 | 1. Get SBI implementation version<br>2. Hart get status (должно быть возможно задавать номер ядра)<br>3. Hart stop<br>4. System Shutdown |

---

## Полезные ссылки

> [!TIP]
> `Ctrl+A`, помимо переключения на монитор QEMU (клавиша `C`), имеет ещё несколько функций. Например, нажатие `X` приведёт к выходу из QEMU.

| Ссылка | Описание |
| --- | --- |
| [docs/riscv-abi.pdf](./docs/riscv-abi.pdf) | RISC-V ABIs Specification |
| [docs/riscv-sbi.pdf](./docs/riscv-sbi.pdf) | RISC-V Supervisor Binary Interface Specification |
| [docs/riscv-spec.pdf](./docs/riscv-spec.pdf) | The RISC-V Instruction Set Manual. Volume I: Unprivileged ISA |
| [operating-system-in-1000-lines.vercel.app](https://operating-system-in-1000-lines.vercel.app/en/) | Operating System in 1,000 Lines |
| [habr.com/ru/articles/874154](https://habr.com/ru/companies/ruvds/articles/874154/) | Операционная система в 1 000 строках кода (перевод) |
| [qemu.org/docs/master/system/riscv/virt.html](https://www.qemu.org/docs/master/system/riscv/virt.html) | Документация по QEMU RISCV32 по системе virt |
| [qemu.org/docs/master/system/mux-chardev.html](https://www.qemu.org/docs/master/system/mux-chardev.html) | QEMU Keys in the character backend multiplexer |
| [github.com/riscv-non-isa/riscv-sbi-doc](https://github.com/riscv-non-isa/riscv-sbi-doc) | Спецификация OpenSBI |
| [github.com/Imtjl/io-systems/opensbi-console-menu](https://github.com/Imtjl/io-systems/tree/main/opensbi-console-menu) | Выполнение 2-го варианта ЛР1 |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
