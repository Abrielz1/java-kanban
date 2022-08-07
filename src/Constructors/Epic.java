package Constructors;

import java.util.ArrayList;
    public class Epic extends Task {
        private ArrayList<Integer> subTaskId;

        Epic(String name, String description, TaskStatus status) {
            super(name, description, status);
        }

        public ArrayList<Integer> getSubtaskId() {
            return subTaskId;
        }

        public void setSubtaskId(ArrayList<Integer> subtaskId) {
            this.subTaskId = subtaskId;
        }

        @Override
        public String toString() {
            return "Constructors.Epic{" +
                    "subTaskId=" + getSubtaskId() +
                    ", id=" + getId() +
                    ", name='" + getTitle() + '\'' +
                    ", description='" + getDescription() + '\'' +
                    ", status='" + getStatus() + '\'' +
                    '}';
        }
    }

