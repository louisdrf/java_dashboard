package com.data;
import com.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReaderDashboard {
    private List<Bilan> bilanList;
    private List<DiagramCompany> clientList;
    private List<DiagramCompany> prestaList;
    private int turnover;

    public List<DiagramCompany> readClientsFromFile(String fileName) {
        File file = new File(fileName);
        clientList = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("client:")) {
                    String[] parts = line.split(":");
                    String clientName = parts[1].split("/")[0].trim();
                    int clientSpending = Integer.parseInt(parts[1].split("/")[1].trim());

                    DiagramCompany client = new DiagramCompany();
                    client.setName(clientName);
                    client.setSpending(clientSpending);
                    clientList.add(client);
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
        }

        return clientList;
    }

    public List<Bilan> readBookingsFromFile(String fileName) {
        File file = new File(fileName);
        bilanList = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("date:")) {
                    String[] parts = line.split(":");
                    String date = parts[1].split("/")[0].trim();
                    int nbbooking = Integer.parseInt(parts[1].split("/")[1].trim());

                    Bilan bilan = new Bilan();
                    bilan.setDate(date);
                    bilan.setNbbooking(nbbooking);
                    bilanList.add(bilan);
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
        }

        return bilanList;
    }

    public List<DiagramCompany> readPrestaFromFile(String fileName) {
        File file = new File(fileName);
        prestaList = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("presta:")) {
                    String[] parts = line.split(":");
                    String prestaName = parts[1].split("/")[0].trim();
                    int prestaSpending = Integer.parseInt(parts[1].split("/")[1].trim());

                    DiagramCompany presta = new DiagramCompany();
                    presta.setName(prestaName);
                    presta.setSpending(prestaSpending);
                    prestaList.add(presta);
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
        }

        return prestaList;
    }

    public int getTurnover(String fileName) {
        File file = new File(fileName);

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("CA:")) {
                    String[] parts = line.split(":");
                    this.turnover = Integer.parseInt(parts[1].split(":")[1].trim());
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
        }
        return this.turnover;
    }

}

