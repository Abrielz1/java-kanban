# java-kanban
Repository for homework project.
Итак
1)
Проблема у меня, 2х видов
а) Если не переопределять вызовы задач по ид:

Происходит, то, что история пустая
при записи в дебаггере это видно
https://github.com/Abrielz1/java-kanban/blob/7add11b978222c36684cefe09e038b8c5a6230a9/src/manager/history/file/FileBackedTasksManager.java#L128

б) если я переопредиляю методы гет
    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return  epic;
    }

    @Override
    public SubTask getSubtaskById(int id) {
        SubTask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }
то, где-то между покладание в лист или между покладанием в мапу, после 1го обращения к истории в нее приходит налл идущий между головой и след узлом
