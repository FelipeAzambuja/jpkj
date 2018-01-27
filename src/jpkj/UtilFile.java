package jpkj;
//teste

import antlr.CharBuffer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.IOUtils;

public class UtilFile {

    static public void deleteDirectory(File path) {
        if (path == null) {
            return;
        }
        if (path.exists()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                    f.delete();
                } else {
                    f.delete();
                }
            }
            path.delete();
        }
    }

    /**
     * Grava um texto em um UtilFile
     *
     * @param UtilFile O caminho + nome do UtilFile
     * @param texto O texto a aser gravado
     */
    public static void writeBIN(String arquivo, byte[] texto) {
        try {
            String filename = arquivo;
            FileOutputStream out = new FileOutputStream(filename);
            out.write(texto);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Grava um texto em um UtilFile
     *
     * @param UtilFile O caminho + nome do UtilFile
     * @param texto O texto a aser gravado
     */
    public static void write(String arquivo, String texto) {
        try {
            FileWriter fw = new FileWriter(arquivo);
            fw.write(texto);
            fw.flush();
            fw.close();
        } catch (Exception e) {
        }
    }

    public static File selectFile(String pasta_inicial, String descricao) {
        if (pasta_inicial.trim().isEmpty()) {
            pasta_inicial = System.getProperty("user.dir");
        }
        return selectFile(pasta_inicial, new String[]{"."}, descricao);
    }

    public static File saveFile(String pasta_inicial, String descricao) {
        if (pasta_inicial.trim().isEmpty()) {
            pasta_inicial = System.getProperty("user.dir");
        }
        JFileChooser jfc = new JFileChooser(pasta_inicial);
        jfc.setDialogType(JFileChooser.SAVE_DIALOG);
        int r = jfc.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        }
        return null;
    }

    /**
     * Inicia um seletor de ARQUIVOS
     *
     * @param pasta_inicial pasta inicial
     * @param filtro final do UtilFile
     * @param descricao descrição do UtilFile
     * @return UtilFile selecionado
     */
    public static File selectFile(String pasta_inicial, String[] filtro, String descricao) {
        if (pasta_inicial.trim().isEmpty()) {
            pasta_inicial = System.getProperty("user.dir");
        }
        final ArrayList sis = new ArrayList();
        sis.addAll(Arrays.asList(filtro));
        final String desc = descricao;
        JFileChooser jfc = new JFileChooser(pasta_inicial, FileSystemView.getFileSystemView());
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                for (int i = 0; i < sis.size(); i++) {
                    if (file.getName().contains(sis.get(i).toString()) || file.isDirectory()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return desc;
            }
        });
        int r = jfc.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        }
        return null;
    }

    /**
     * Inicia um seletor de PASTAS
     *
     * @param pasta_inicial pasta inicial
     * @return Pasta selecionada
     */
    public static File selectFolder(String pasta_inicial) {
        JFileChooser jfc = new JFileChooser(pasta_inicial);
        jfc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Pastas";
            }
        });
        int r = jfc.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        }
        return null;
    }

    /**
     * Verifica se o UtilFile ou pasta existe
     *
     * @param UtilFile Objeto do tipo file.
     * @return True para se o UtilFile ou pasta existe.
     */
    public static boolean exists(File arquivo) {
        return arquivo.exists();
    }

    /**
     * Verifica se o UtilFile ou pasta existe
     *
     * @param UtilFile Texto com o caminho do UtilFile ou pasta
     * @return true para se o UtilFile ou pasta existe.
     */
    public static boolean exists(String arquivo) {
        return new File(arquivo).exists();
    }

    /**
     * Carrega todo o UtilFile em uma String.
     *
     * @param UtilFile Caminho com o nome do UtilFile
     * @return Todo o conteudo do UtilFile.
     */
    public static String load(File arquivo) {
        String retorno = "";
        try {
            retorno = load(arquivo.getPath());
        } catch (Exception e) {
        }
        return retorno;
    }

    public static byte[] loadBIN(File arquivo) {
        byte[] b = null;
        try {
            b = IOUtils.toByteArray(new FileInputStream(arquivo));
        } catch (Exception e) {
        }
        return b;
    }

    /**
     * Carrega todo o UtilFile em uma String.
     *
     * @param UtilFile Caminho com o nome do UtilFile
     * @return Uma lista com todo o conteudo do UtilFile.
     */
    public static ArrayList loadLines(File arquivo) {
        ArrayList retorno = new ArrayList();
        try {
            FileReader fr = new FileReader(arquivo);
            BufferedReader br = new BufferedReader(fr);
            String linha = null;
            while ((linha = br.readLine()) != null) {//Em teoria só é null no final
                retorno.add(linha);
            }
        } catch (Exception e) {
        }
        return retorno;
    }

    /**
     * Carrega todo o UtilFile em uma String.
     *
     * @param UtilFile Caminho com o nome do UtilFile
     * @return Todo o conteudo do UtilFile.
     */
    public static String load(String arquivo) {
        String retorno = "";
        try {

            FileInputStream stream = new FileInputStream(arquivo);
            FileChannel channel = stream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());

            String text = "";
            while (channel.read(buffer) != -1) {
                buffer.flip();
                java.nio.CharBuffer charBuffer = Charset.forName("UTF-8").decode(buffer);
                text = charBuffer.toString();
                buffer.clear();
            }
            retorno = text;
           channel.close();
           stream.close();
//   FileReader fr = new FileReader(arquivo);
//   BufferedReader br = new BufferedReader(fr);   
//   String linha = null;
//   while ((linha = br.readLine()) != null) {
//    retorno += linha + "\n";
//   }
//   br.close();   
        } catch (Exception e) {
        }
        return retorno;
    }

    /**
     * Carrega todo o UtilFile em uma String.
     *
     * @param UtilFile Caminho com o nome do UtilFile
     * @return Uma lista com full o conteudo do UtilFile.
     */
    public static ArrayList loadLines(String arquivo) {
        ArrayList retorno = new ArrayList();
        try {
            FileReader fr = new FileReader(arquivo);
            BufferedReader br = new BufferedReader(fr);
            String linha = null;
            while ((linha = br.readLine()) != null) {//Em teoria só é null no final
                retorno.add(linha);
            }
        } catch (Exception e) {
        }
        return retorno;
    }

    /**
     * Redimenciona o UtilFile de imagem em formato jpg
     *
     * @param imagem
     * @param largura
     * @param altura
     */
    public static void resizeImage(File imagem, int largura, int altura) {
        try {
            BufferedImage imagemm = ImageIO.read(imagem);
            int new_w = largura, new_h = altura;
            BufferedImage new_img = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagemm, 0, 0, new_w, new_h, null);
            String formato = imagem.getName().substring(imagem.getName().indexOf(".") + 1).toUpperCase();
            ImageIO.write(new_img, formato, imagem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Redimenciona o UtilFile de imagem em formato jpg
     *
     * @param imagem
     * @param largura
     * @param altura
     */
    public static void resizeImage(File imagem, int largura, int altura, String novo_arquivo) {
        try {
            BufferedImage imagemm = ImageIO.read(imagem);
            int new_w = largura, new_h = altura;
            BufferedImage new_img = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagemm, 0, 0, new_w, new_h, null);
            String formato = imagem.getName().substring(imagem.getName().indexOf(".") + 1).toUpperCase();
            File nf = new File(novo_arquivo);
            ImageIO.write(new_img, formato, nf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel openImage(final File image) {
        JPanel retorno = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    BufferedImage img = ImageIO.read(image);
                    g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return retorno;
    }

    public static void copy(String origem, String destino) {
        try {
            File source = new File(origem);
            File destination = new File(destino);

            if (destination.exists()) {
                destination.delete();
            }

            FileChannel sourceChannel = null;
            FileChannel destinationChannel = null;

            try {
                sourceChannel = new FileInputStream(source).getChannel();
                destinationChannel = new FileOutputStream(destination).getChannel();
                sourceChannel.transferTo(0, sourceChannel.size(),
                        destinationChannel);
            } finally {
                if (sourceChannel != null && sourceChannel.isOpen()) {
                    sourceChannel.close();
                }
                if (destinationChannel != null && destinationChannel.isOpen()) {
                    destinationChannel.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
