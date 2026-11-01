package es.studium.ejercicio05;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class LectorXMLDOM {
    private static final String FILE_NAME = "libros.xml";

    public static void main(String[] args) {
        // 0. Generamos el XML inicial con los libros y el quinto libro añadido
        generateXmlFile();

        // 1. Cargar el documento mediante DocumentBuilder y DOM
        try {
            File xmlFile = new File(FILE_NAME);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Normalizar el documento (evita problemas con espacios en blanco)
            doc.getDocumentElement().normalize();

            // 2. Mostrar el elemento raíz y los datos de cada libro
            Element root = doc.getDocumentElement();
            System.out.println("====== Elemento Raíz =======");
            System.out.println("Raíz: " + root.getNodeName());

            NodeList nList = doc.getElementsByTagName("libro");
            System.out.println("\n====== Listado de Libros =======");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String titulo = element.getElementsByTagName("titulo").item(0).getTextContent();
                    String autor = element.getElementsByTagName("autor").item(0).getTextContent();
                    String publicadoEn = element.getAttribute("publicado_en");

                    System.out.println("- Título: " + titulo + " | Autor: " + autor + " | Publicado en: " + publicadoEn);
                }
            }

            // 3. Mostrar número total de libros y filtrar los publicados desde 1900 en adelante
            System.out.println("\n====== Estadísticas y Filtro (>= 1900) =======");
            System.out.println("Número total de libros: " + nList.getLength());

            int countModernos = 0;
            System.out.println("Libros publicados desde 1900 en adelante:");
            for (int i = 0; i < nList.getLength(); i++) {
                Element element = (Element) nList.item(i);
                int anio = Integer.parseInt(element.getAttribute("publicado_en"));

                if (anio >= 1900) {
                    String titulo = element.getElementsByTagName("titulo").item(0).getTextContent();
                    System.out.println(" -> " + titulo + " (" + anio + ")");
                    countModernos++;
                }
            }
            System.out.println("Total de libros desde 1900: " + countModernos);

        } catch (Exception e) {
            System.err.println("Error al procesar el fichero XML: " + e.getMessage());
        }
    }

    private static void generateXmlFile() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Elemento Raíz <biblioteca>
            Element rootElement = doc.createElement("biblioteca");
            doc.appendChild(rootElement);

            // Datos de los libros (incluyendo el 5º libro solicitado)
            String[][] librosData = {
                    {"El Capote", "Nikolai Gogol", "1842"},
                    {"Don Quijote", "Miguel de Cervantes", "1605"},
                    {"El Principito", "Antoine de Saint-Exupéry", "1943"},
                    {"El nombre de la rosa", "Umberto Eco", "1980"},
                    {"El Señor de los Anillos", "J.R.R. Tolkien", "1954"} // 5º Libro
            };

            for (String[] datos : librosData) {
                Element libro = doc.createElement("libro");
                libro.setAttribute("publicado_en", datos[2]);

                Element titulo = doc.createElement("titulo");
                titulo.setTextContent(datos[0]);
                libro.appendChild(titulo);

                Element autor = doc.createElement("autor");
                autor.setTextContent(datos[1]);
                libro.appendChild(autor);

                rootElement.appendChild(libro);
            }

            // Volcar la estructura DOM a un fichero físico XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_NAME));
            transformer.transform(source, result);

        } catch (Exception e) {
            System.err.println("Error al crear el XML: " + e.getMessage());
        }
    }
}