# Business-logic-of-program-systems
<div id="_blportletlab3_WAR_blportlet_bl-lab3-text">
<h3>Внимание! У разных вариантов разный текст задания!</h3>
<p>Переработать программу, созданную в результате выполнения лабораторной работы #3, следующим образом:</p> 
<ol><li>Для управления бизнес-процессом использовать BPM-движок Camunda.
</li>
<li>Заменить всю "статическую" бизнес-логику на "динамическую" на базе BPMS. Весь бизнес-процесс, реализованный в ходе выполнения предыдущих лабораторных работ (включая разграничение доступа по ролям, управление транзакциями, асинхронную обработку и периодические задачи), должен быть сохранён!.
</li>
<li>BPM-движок должен быть запущен в режиме standalone-сервиса.
</li><li>Для описания бизнес-процесса необходимо использовать приложение Camunda Modeler.
</li>
<li>Пользовательский интерфейс приложения должен быть сгенерирован с помощью генератора форм Camunda.
</li>
<li>Итоговая сборка должно быть развёрнута на сервере helios под управление сервера приложений WildFly.
</li>


</ol> 

<p><b>Правила выполнения работы:</b></p>
<ol><li>
Описание бизнес-процесса необходимо реализовать на языке BPMN 2.0.
</li>
<li>
Необходимо интегрировать в состав процесса, управляемого BPMS, всё, 
что в принципе возможно в него интегрировать. Если какой-то из компонентов 
архитектуры приложения (например, асинхронный обмен сообщениями с помощью JMS) не поддерживается, необходимо использовать для интеграции с этой подсистемой соответствующие API и адаптеры.
</li>
<li>
Распределённую обработку задач и распределённые транзакции на BPM-движок переносить не требуется.
</li></ol></div>