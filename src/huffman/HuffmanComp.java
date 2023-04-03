package huffman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;





public class HuffmanComp {

	public static void main(String[] args) {
		

		
		
		//initialisation d'une variable qui retourne le nombre de caractere;
        int combien_caractere=0;
		//Mettre ici le nom du fichier à compresser ensuite le nom du qu nouveau fichier compressé(ne pas oublier les extension .txt et .bin)
        //String fileName = "textesimple.txt";
		//String fileCompName="textesimple_comp.bin";
        // Lecture du fichier
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Entrez le nom du fichier : ");
        String fileName = scanner.nextLine()+".txt";
        
        System.out.print("Entrez le nom du fichier de compression : ");
        String fileCompName = scanner.nextLine()+".bin";
        scanner.close();
        Scanner lecture = null;
        try {
            lecture = new Scanner(new File(fileName),"UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        StringBuilder sb = new StringBuilder();
        while (lecture.hasNextLine()) {
            sb.append(lecture.nextLine());
        }
        lecture.close();

        String text = sb.toString();

        // Créer un arbre (feuille) pour chaque caractère de l'alphabet avec la fréquence associée
        HashMap<Character, Integer> freqTable = new HashMap<Character, Integer>();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            freqTable.put(ch, freqTable.getOrDefault(ch, 0) + 1);
            
        }
        //creation d'une PriorityQueue de type Node 
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        
        for (char ch : freqTable.keySet()) {
            pq.offer(new Node(ch, freqTable.get(ch)));
            
            combien_caractere+=freqTable.get(ch) ;
            
        }
        System.out.println("nombre carac tot : "+combien_caractere);
        

        // Construire l'arbre
        while (pq.size() > 1) {
            Node t1 = pq.poll();
            Node t2 = pq.poll();
            Node t = new Node(t1.freq + t2.freq, t1, t2);
            pq.add(t);
        }
        
        

        // Génération de codes binaires pour chaque caractère dans la chaîne de texte
        Node root = pq.poll();
        HashMap<Character, String> codes = new HashMap<>();
        generateCodes(root, "", codes);
        System.out.println("Codes des caracteres :");
        // Afficher les codes des caractères
        for (char ch : codes.keySet()) {
            System.out.println(ch + " : " + codes.get(ch));
            
        }
        //On a le code binaire 
        StringBuilder compressed = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            compressed.append(codes.get(ch));
        }

        // Enregistrement du fichier compressé
        try (FileOutputStream fos = new FileOutputStream(fileCompName)) {
            String compressedStr = compressed.toString();
            while (compressedStr.length() > 0) {
                if (compressedStr.length() < 8) {
                    compressedStr += "00000000".substring(compressedStr.length());
                }
                int b = Integer.parseInt(compressedStr.substring(0, 8), 2);
                fos.write(b);
                compressedStr = compressedStr.substring(8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        //taux de compression
        String originalFilePath = fileName;
        String compressedFilePath = fileCompName;

        // Récupérer la taille du fichier original
        File originalFile = new File(originalFilePath);
        long originalFileSize = originalFile.length();

        // Récupérer la taille du fichier compressé
        File compressedFile = new File(compressedFilePath);
        long compressedFileSize = compressedFile.length();

        // Détermination du taux de compression
        double compressionRatio = 1 - (double)compressedFileSize / originalFileSize;

        System.out.println("Taille du fichier original: " + originalFileSize + " octets");
        System.out.println("Taille du fichier compresse: " + compressedFileSize + " octets");
        System.out.println("Taux de compression: " + compressionRatio);
        
        
        
        //determination de la moyenne de bits d'un caractere
        double avgBitsParCarac=(double)compressedFileSize*8 /combien_caractere;
        System.out.println("Nombre moyen de bits de stockage d'un caractere du texte compresse : " + avgBitsParCarac);
        
        
        
        	
	}
	//Méthode pour parcourir l'arbre de Huffman
	 private static void generateCodes(Node node, String code, HashMap<Character, String> codes) {
	        if (node.isLeaf()) {
	        	codes.put(node.ch, code);
	            //System.out.println( codes.put(node.ch, code));
	        } else {
	            generateCodes(node.left, code + "0", codes);
	            generateCodes(node.right, code + "1", codes);
	        }
	    }
	
}
