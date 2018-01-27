package jpkj;

import java.net.*;
import java.io.*;
import org.apache.commons.io.IOUtils;

public class P2P {

    int socketsAtivos = 0;
    ServerSocket ss = null;
    boolean vida = true;

    public String execute(String conteudo, String origem) {
        return "";
    }

    public P2P(final int porta) {
        new Thread() {
            Socket s = null;
            ServerSocket ss = null;

            @Override
            public void run() {
                try {
                    ss = new ServerSocket(porta);
                } catch (IOException iOException) {
                    Log.out(Util.exeptionToString(iOException));
                }
                s = null;
                //
                while (vida) {
                    try {
                        if (!ss.isClosed()) {
                            ss.close();
                        }
                        ss = new ServerSocket(porta);
                        s = ss.accept();
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    if (s.isClosed() && !s.isConnected()) {
                                        return;
                                    }
                                    InputStream input = s.getInputStream();
                                    OutputStream output = s.getOutputStream();
                                    BufferedReader br = new BufferedReader(new InputStreamReader(input));
                                    String total = "";
                                    String header = "";
                                    do {
                                        total += br.readLine() + "\r\n";
                                    } while (br.ready());
                                    PrintWriter pw = new PrintWriter(output, true);

                                    String resposta = execute(total.trim(), s.getInetAddress().getHostAddress());
                                    pw.write(resposta.trim());
                                    pw.flush();
                                    s.close();
                                    Thread.sleep(5);
                                } catch (Exception iOException) {
                                    Log.out(Util.exeptionToString(iOException));
                                } finally {
                                }
                            }
                        }.start();
                    } catch (Exception e) {
                        Log.out(Util.exeptionToString(e));
                    } finally {
                    }
                }
            }//thread
        }.start();//Thread
    }

    public static String send(String host, int porta, String conteudo) {
        String retorno = "Sem resposta";
        try {
            conteudo = "\r\n" + conteudo;
            Socket conexao = new Socket(host, porta);
            PrintStream ps = new PrintStream(conexao.getOutputStream());
            ps.println(conteudo);
            BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha = "", total = "";
            while ((linha = br.readLine()) != null) {
                total += linha;
            }
            br.close();
            retorno = total;
            ps.flush();
            ps.close();
            conexao.close();
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
