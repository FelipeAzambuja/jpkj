package jpkj;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.table.*;
import javax.swing.*;
import org.apache.commons.codec.binary.Hex;

public class Db {

    public static enum Tipos {

        SQLITE,
        MYSQL,
        HSQLDB,
        POSTGRESQL,
        FIREBIRD,
        ORACLE,
        SQLSERVER,
        ODBC,
        SQLANYWHERE

    };

    public String Arquivo = "";
    public static Tipos tipo;
    public Connection conexao = null;

    public Db ( String url, Properties p ) throws Exception {
        this.url = url;
        this.usuario = "";
        this.senha = "";
        this.p = p;
        this.conexao = Db.newConnection ( url, "", "", p );
    }

    public Db ( String url ) throws Exception {
        this.url = url;
        this.usuario = "";
        this.senha = "";
        this.p = null;
        this.conexao = Db.newConnection ( url );
    }

    public Db ( String url, String usuario, String senha ) throws Exception {
        this.url = url;
        this.usuario = "";
        this.senha = "";
        this.p = null;
        this.conexao = Db.newConnection ( url, usuario, senha, null );
    }

    public void renew () throws Exception {
        this.conexao = Db.newConnection ( this.url, this.usuario, this.senha, this.p );
    }

    public Connection getConnection () {
        return conexao;
    }

    /**
     * Método de conexão em geral teste
     *
     * @param url jdbc:mysql://host:porta/base para odf do libre office use
     * odb:caminho
     * @param usuario usuário do banco de dados
     * @param senha senha do usuário do banco de dados
     * @return retorna uma conexão
     */
    public void connect ( String url, String usuario, String senha ) throws Exception {
        this.conexao = newConnection ( url, usuario, senha, null );
    }

    public void connect ( String url ) throws Exception {
        this.conexao = newConnection ( url );
    }

    public static Connection newConnection ( String url ) throws Exception {
        return newConnection ( url, "", "", null );
    }

    public String url = "";
    public String usuario = "";
    public String senha = "";
    public Properties p = null;

    /**
     * Retorna uma nova conexao
     *
     * @param url jdbc:mysql://host:porta/base
     * @param usuario usuário do banco de dados
     * @param senha senha do usuário do banco de dados
     * @return retorna uma conexão
     */
    public static Connection newConnection ( String url, String usuario, String senha, Properties p ) throws Exception {
        Tipos tipo = null;
        String classe = "NADA";
        if ( url.contains ( "sqlite" ) ) {
            classe = "org.sqlite.JDBC";
            tipo = Tipos.SQLITE;
        }
        if ( url.contains ( "mysql" ) ) {
            classe = "com.mysql.jdbc.Driver";
            tipo = Tipos.MYSQL;
        }
        if ( url.contains ( "hsqldb" ) ) {
            classe = "org.hsqldb.jdbcDriver";
//            if (!tipo.equals("ODB")) {
            tipo = Tipos.HSQLDB;
//            }
        }
        if ( url.contains ( "firebird" ) ) {
            classe = "org.firebirdsql.jdbc.FBDriver";
            tipo = Tipos.FIREBIRD;
        }
        if ( url.contains ( "postgre" ) ) {
            classe = "org.postgresql.Driver";
            tipo = Tipos.POSTGRESQL;
        }
        if ( url.contains ( "oracle" ) ) {
            classe = "oracle.jdbc.OracleDriver";
            tipo = Tipos.ORACLE;
        }
        if ( url.contains ( "sqlserver" ) ) {
            classe = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            tipo = Tipos.SQLSERVER;
            System.out.println ( "A conexão so deve passar a url no seguinte formato.\njdbc:sqlserver://IP.DAMAQUINA:PORTA;databaseName=NOME_DO_DB;user=USUARIO;password=SENHA os outros paramentros serão ignorados" );
        }
        if ( url.contains ( "odbc" ) ) {
            classe = "sun.jdbc.odbc.JdbcOdbcDriver";
            tipo = Tipos.ODBC;
        }
        if ( url.contains ( "sqlanywhere" ) ) {
            classe = "sybase.jdbc4.sqlanywhere.IDriver";
            tipo = Tipos.SQLANYWHERE;
        }
        try {
            if (  ! classe.equals ( "NADA" ) ) {
                Class.forName ( classe ).newInstance ();
            } else {
                System.out.println ( "Base de dados desconhecida mande um e-mail para felipe@felipeazambuja.com.br" );
            }
        } catch ( Exception e ) {
        } finally {

        }
        if ( p != null ) {
            try {
                return DriverManager.getConnection ( url, p );
            } catch ( SQLException ex ) {
                ex.printStackTrace ();
            } finally {

            }
        } else {

            if ( tipo.equals ( "SQLSERVER" ) || tipo.equals ( "SQLITE" ) ) {
                if ( tipo.equals ( "SQLSERVER" ) ) {
                    return DriverManager.getConnection ( url );
                } else if (  ! usuario.isEmpty () &&  ! senha.isEmpty () ) {
                    return DriverManager.getConnection ( url, usuario, senha );
                } else if (  ! usuario.isEmpty () ) {
                    return DriverManager.getConnection ( url, usuario, "" );
                } else {
                    return DriverManager.getConnection ( url );
                }
            } else {
                return DriverManager.getConnection ( url, usuario, senha );
            }

        }
        return null;
    }

    /**
     * Desconecta a conexao do framework
     *
     * @param con Conexão a ser fechada.
     */
    public void unConnect () {
        try {
            conexao.close ();
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
    }

    /**
     * Transforma linhas de uma tabela em instruções sql
     *
     * @param tabela Nome da JTable por exemplo jTable1
     * @param nomeTABELA Nome da tabela do sql por exemplo pessoas
     * @param camposSQL Lista com o nome dos campos exemplo new
     * String[]{"nome","telefone"};
     * @param camposTABELA Lista com os indices referentes aos campos sql por
     * exemplo new int[]{1,2}
     * @return Retorna um ArrayList com as instruções SQL.
     */
    public static List<String> getSQLTable ( JTable tabela, String nomeTABELA, String[] camposSQL, int[] camposTABELA ) {
        List sqls = new List ();
        try {
            String sql_atual = null;
            int quantidade_linhas_tabela = tabela.getRowCount ();
            int quantidade_colunas = camposTABELA.length;
            for ( int i = 0; i < quantidade_linhas_tabela; i ++ ) {
                sql_atual = "insert into " + nomeTABELA + " (";
                for ( int j = 0; j < camposSQL.length; j ++ ) {
                    if ( j == ( camposSQL.length - 1 ) ) {
                        sql_atual += camposSQL[ j ] + ") values (";
                    } else {
                        sql_atual += "" + camposSQL[ j ] + ",";
                    }
                }
                for ( int j = 0; j < quantidade_colunas; j ++ ) {
                    if ( j == ( camposSQL.length - 1 ) ) {
                        sql_atual += tabela.getValueAt ( i, camposTABELA[ j ] ) + ")";
                    } else {
                        sql_atual += "'" + tabela.getValueAt ( i, camposTABELA[ j ] ) + "',";
                    }
                }
                sqls.add ( sql_atual );
            }
            return sqls;
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
        return null;
    }

    /**
     * Atribui uma tabela um select
     *
     * @param table jtable
     * @param sql string sql
     */
    public void setSqlTable ( javax.swing.JTable table, String sql ) {
        try {
            DefaultTableModel modelo_tabela = ( DefaultTableModel ) table.getModel ();
            Statement s = conexao.createStatement ();
            ResultSet rs = s.executeQuery ( sql );
            ResultSetMetaData rsmd = rs.getMetaData ();
            int quantCOLUNAS = rsmd.getColumnCount ();
            List nomeCOLUNAS = new List ();
            int contador = 0;
            while ( quantCOLUNAS > contador ) {
                contador ++;
                nomeCOLUNAS.add ( rsmd.getColumnLabel ( contador ) );
            }
            modelo_tabela.setColumnCount ( 0 );
            contador = 0;
            while ( quantCOLUNAS > contador ) {
                modelo_tabela.addColumn ( nomeCOLUNAS.get ( contador ) );
                contador ++;
            }
            modelo_tabela.setRowCount ( 0 );
            List linha = new List ();
            while ( rs.next () ) {
                linha.clear ();
                contador = 0;
                while ( quantCOLUNAS > contador ) {
                    linha.add ( rs.getString ( nomeCOLUNAS.get ( contador ).toString () ) );
                    contador ++;
                }
                modelo_tabela.addRow ( linha.toArray () );
            }
            table.setModel ( modelo_tabela );
            rs.close ();
            s.close ();
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
    }

    /**
     * Mesma coisa que o setSQLtabela porem usa como titulo o arraylist
     *
     * @param table tabela
     * @param sql sql
     * @param lista titulos
     */
    public void setSqlTable ( javax.swing.JTable table, String sql, String[] lista ) {
        try {
            DefaultTableModel modelo_tabela = ( DefaultTableModel ) table.getModel ();
            Statement s = conexao.createStatement ();
            ResultSet rs = s.executeQuery ( sql );
            ResultSetMetaData rsmd = rs.getMetaData ();
            int quantCOLUNAS = rsmd.getColumnCount ();
            List nomeCOLUNAS = new List ();
            int contador = 0;
            while ( quantCOLUNAS > contador ) {
                contador ++;
                nomeCOLUNAS.add ( rsmd.getColumnLabel ( contador ) );
            }
            modelo_tabela.setColumnCount ( 0 );
            contador = 0;
            while ( quantCOLUNAS > contador ) {
                //contador++;
                modelo_tabela.addColumn ( lista[ contador ] );
                contador ++;
            }
            modelo_tabela.setRowCount ( 0 );
            List linha = new List ();
            while ( rs.next () ) {
                linha.clear ();
                contador = 0;
                while ( quantCOLUNAS > contador ) {
                    linha.add ( rs.getString ( nomeCOLUNAS.get ( contador ).toString () ) );
                    contador ++;
                }
                modelo_tabela.addRow ( linha.toArray () );
            }
            table.setModel ( modelo_tabela );
            rs.close ();
            s.close ();
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
    }

    /**
     * Mesma coisa que o setSQLtabela2 porém usa um resultset ao invez de um
     * texto com sql
     *
     * @param table tabela
     * @param r resultado já filtrado
     * @param lista campos da tabela
     */
    public static void setSqlTable ( javax.swing.JTable table, ResultSet r, String[] lista ) {
        try {
            DefaultTableModel modelo_tabela = ( DefaultTableModel ) table.getModel ();
            ResultSet rs = r;
            ResultSetMetaData rsmd = rs.getMetaData ();
            int quantCOLUNAS = rsmd.getColumnCount ();
            List nomeCOLUNAS = new List ();
            int contador = 0;
            while ( quantCOLUNAS > contador ) {
                contador ++;
                nomeCOLUNAS.add ( rsmd.getColumnLabel ( contador ) );
            }
            modelo_tabela.setColumnCount ( 0 );
            contador = 0;
            while ( quantCOLUNAS > contador ) {
                //contador++; Opa :P
                modelo_tabela.addColumn ( lista[ contador ] );
                contador ++;
            }
            modelo_tabela.setRowCount ( 0 );
            List linha = new List ();
            while ( rs.next () ) {
                linha.clear ();
                contador = 0;
                while ( quantCOLUNAS > contador ) {
                    linha.add ( rs.getString ( nomeCOLUNAS.get ( contador ).toString () ) );
                    contador ++;
                }
                modelo_tabela.addRow ( linha.toArray () );
            }
            table.setModel ( modelo_tabela );
            rs.close ();
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
    }

    /**
     * Atribui a um combo o valor de um campo do select
     *
     * @param combo javax.jComboBox
     * @param sql String sql
     */
    public void setSqlCombo ( javax.swing.JComboBox combo, String sql ) {
        try {
            DefaultComboBoxModel modelo_combo = ( DefaultComboBoxModel ) combo.getModel ();
            modelo_combo.removeAllElements ();
            Statement s = conexao.createStatement ();
            ResultSet rs = s.executeQuery ( sql );
            while ( rs.next () ) {
                modelo_combo.addElement ( rs.getString ( 1 ) );
            }
            combo.setModel ( modelo_combo );
            rs.close ();
            s.close ();
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
    }

    /**
     * Executa uma instrução SQL de forma direta e insegura
     *
     * @param sql String sql
     */
    public boolean exec ( String sql ) {
        boolean retorno = false;
        try {
            Statement s = conexao.createStatement ();
            retorno = s.execute ( sql );
            s.close ();
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
        return retorno;
    }

    public boolean Texec ( String sql ) throws SQLException {
        boolean retorno = false;
        Statement s = conexao.createStatement ();
        retorno = s.execute ( sql );
        s.close ();
        return retorno;
    }

    public boolean exec ( String sql, String[] parametros ) {
        return prepareCommand ( sql, parametros );
    }

    /**
     * Executa uma instrução SQL retornando um Objeto resultSet para seu uso.
     *
     * @param sql
     * @return Objeto resultSet para seu uso.
     */
    public ResultSet ret ( String sql ) {
        ResultSet r = null;
        try {
            Statement s = conexao.createStatement ();
            r = s.executeQuery ( sql );
//            s.close();
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
        return r;
    }

    /**
     * Identico ao executa porém de forma segura
     *
     * @param sql
     * @param argumentos Uma lista que substituirá de forma segura os =? do SQL
     *
     */
    public boolean prepareCommand ( String sql, String[] argumentos ) {
        boolean retorno = true;
        try {
            PreparedStatement ps = conexao.prepareStatement ( sql );
            for ( int contador = 0; contador < argumentos.length; contador ++ ) {
                int ips = contador + 1;
                ps.setString ( ips, argumentos[ contador ] );
            }
            if ( tipo == Tipos.POSTGRESQL ) {
                retorno = exec ( ps.toString () );
            } else {
                retorno = ps.execute ();
            }
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
        return retorno;
    }

    /**
     * Mistura de preparaexecuta com retorna
     *
     * @param sql
     * @param argumentos
     * @return um objeto ResultSet
     */
    public ResultSet prepareResultSet ( String sql, String[] argumentos ) {
        ResultSet r = null;
        try {
            PreparedStatement ps = conexao.prepareStatement ( sql );
            for ( int contador = 0; contador < argumentos.length; contador ++ ) {
                int ips = contador + 1;
                ps.setObject ( ips, argumentos[ contador ] );
            }
            if ( tipo == Tipos.POSTGRESQL ) { //bancos que o driver usar server size validation
                r = ret ( ps.toString () );
            } else {
                r = ps.executeQuery ();
            }
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
        return r;
    }

    public List<Map<String, String>> get ( String sql, String[] argumentos ) {
        return get ( prepareResultSet ( sql, argumentos ) );
    }

    public List<Map<String, String>> get ( String sql ) {
        return get ( ret ( sql ) );
    }

    public static List<Map<String, String>> get ( ResultSet r ) {
        List<Map<String, String>> retorno = new List<Map<String, String>> ();
        try {
            ResultSetMetaData re = null;
            String nameColumn = "";
            String valueColumn = "";
            while ( r.next () ) {
                re = r.getMetaData ();
                Map<String, String> m = new Map<> ();
                int countColumn = re.getColumnCount ();
                for ( int i = 1; i <= countColumn; i ++ ) {
                    nameColumn = re.getColumnLabel ( i );
                    valueColumn = r.getString ( i );
                    m.put ( nameColumn, valueColumn );
                }
                retorno.add ( m );
            }
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
        return retorno;
    }

    public List<Map<String, String>> Tget ( String sql, String[] argumentos ) throws Exception {
        return get ( prepareResultSet ( sql, argumentos ) );
    }

    public List<Map<String, String>> Tget ( String sql ) throws Exception {
        return get ( ret ( sql ) );
    }

    public static List<Map<String, String>> Tget ( ResultSet r ) throws Exception {
        List<Map<String, String>> retorno = new List<Map<String, String>> ();
        ResultSetMetaData re = null;
        String nameColumn = "";
        String valueColumn = "";
        while ( r.next () ) {
            re = r.getMetaData ();
            Map<String, String> m = new Map<> ();
            int countColumn = re.getColumnCount ();
            for ( int i = 1; i <= countColumn; i ++ ) {
                nameColumn = re.getColumnLabel ( i );
                valueColumn = r.getString ( i );
                m.put ( nameColumn, valueColumn );
            }
            retorno.add ( m );
        }
        return retorno;
    }

    public static String createTable ( Class c ) {
        String sql = "create table " + c.getSimpleName () + " (";
        Field[] declaredFields = c.getDeclaredFields ();
        for ( Field declaredField : declaredFields ) {
            String name = declaredField.getName ();
            String typeName = declaredField.getType ().getName ();
            //<tabela de conversão de tipos>
            if ( typeName.equals ( "java.lang.String" ) ) {
                typeName = "TEXT";
            } else if ( typeName.equals ( "java.lang.Integer" ) ) {
                typeName = "INTEGER";
            } else if ( typeName.equals ( "java.lang.Boolean" ) ) {
                typeName = "TEXT";
            } else if ( typeName.equals ( "java.lang.Float" ) ) {
                if ( tipo == Tipos.SQLITE ) {
                    typeName = "NUMERIC";
                } else {
                    typeName = "FLOAT";
                }
            } else if ( typeName.equals ( "jpkj.Calendar" ) ) {
                typeName = "TIMESTAMP";
            } else {
                typeName = "INTEGER";
            }
            //</tabela de conversão de tipos>
            String adds = "";
            Annotation[] annotations = declaredField.getAnnotations ();
            for ( Annotation annotation : annotations ) {
                String annotationName = annotation.annotationType ().getName ();
                //<tabela de anotações>
                if ( annotationName.equals ( "jpkj.annotation.Type" ) ) {
                    jpkj.annotation.Type forcedType = ( jpkj.annotation.Type ) annotation;
                    typeName = forcedType.value ();
                } else if ( annotationName.equals ( "jpkj.annotation.Id" ) ) {
                    jpkj.annotation.Id id = ( jpkj.annotation.Id ) annotation;
                    if ( id.isPk () ) {
                        adds += " PRIMARY KEY ";
                    }
                    if ( id.isAI () ) {
                        if ( tipo == Tipos.MYSQL ) {
                            adds += " AUTO_INCREMENT ";
                        } else {
                            adds += " AUTOINCREMENT ";
                        }
                    }
                } else if ( annotationName.equals ( "jpkj.annotation.Default" ) ) {
                    jpkj.annotation.Default defaulte = ( jpkj.annotation.Default ) annotation;
                    adds += "DEFAULT " + defaulte.value () + " ";
                }
                //</tabela de anotações>
            }
            sql += name + " " + typeName + " " + adds + " " + ",";
        }
        sql = sql.substring ( 0, sql.length () - 1 );
        sql += ")";
        return sql;
    }

    /**
     * Retorna uma lista com uma lista que tem as linhas da tabela.
     *
     * @param r ResultSet NÂO VAI ACHAR QUE È STRING SQL ANIMAL
     * @return uma lista com uma lista que tem as linhas da tabela.
     */
    public static List<List<String>> getArray ( ResultSet r ) {
        List<List<String>> l = new List<List<String>> ();//tchau jdk6 só que não
        try {
            ResultSetMetaData re = r.getMetaData ();
            while ( r.next () ) {
                List<String> li = new List<String> ();
                for ( int i = 1; i <= re.getColumnCount (); i ++ ) {
                    li.add ( r.getString ( i ) );
                }
                l.add ( li );
            }
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
        return l;
    }

    /**
     * Retorna uma lista com uma lista que tem as linhas da tabela.
     *
     * @param sql instrução SQL
     * @return uma lista com uma lista que tem as linhas da tabela.
     */
    public List<List<String>> getArray ( String sql ) {
        return getArray ( ret ( sql ) );
    }

    /**
     * Retorna o texto com a instrução sql
     *
     * @param tabela nome da tabela
     * @param campos campos separados por virgula
     * @param nomes nome dos campos
     * @param valores valores dos campos
     * @return texto com a instrução sql
     */
    public static String Select ( String tabela, String campos, Object[] nomes, Object[] valores ) {
        String sql = "select " + campos + " from " + tabela + " where ";
        for ( int i = 0; i < nomes.length; i ++ ) {
            String valor = valores[ i ].toString ();
            String nome = nomes[ i ].toString ();
            if ( i < nomes.length - 1 ) {
                sql += nome + "'" + valor.replaceAll ( Pattern.quote ( "'" ), "" ) + "'" + " and ";
            } else {
                sql += nome + "'" + valor.replaceAll ( Pattern.quote ( "'" ), "" ) + "'";
            }
        }
        return sql;
    }

    /**
     * Retorna o texto com a instrução sql
     *
     * @param tabela nome da tabela
     * @param where condicao
     * @return texto com a instrução sql
     */
    public static String Delete ( String tabela, String where ) {
        String sql = "delete from " + tabela + " where " + where;
        return sql;
    }

    public static String Update ( String tabela, Map<String, String> mapa, String where ) {
        List<String> values = new List<String> ();
        for ( Object key : mapa.getKeys () ) {
            values.add ( mapa.toString ( key ) );
        }
        return Update ( tabela, mapa.getKeys ().toArray (), values.toStringArray (), where );
    }

    /**
     * Retorna um texto de um update na tabela.
     *
     * @param tabela nome da tabela
     * @param nomes nome dos campos
     * @param valores valores dos campos
     * @param where condição de filtro para a atualização
     * @return Texto com a instrução sql
     */
    public static String Update ( String tabela, Object[] nomes, Object[] valores, String where ) {
        String sql = "update " + tabela + " set ";
        for ( int i = 0; i < nomes.length; i ++ ) {
            String valor = valores[ i ].toString ();
            String nome = nomes[ i ].toString ();
            if ( i < nomes.length - 1 ) {
                sql += nome + " = '" + valor.replaceAll ( Pattern.quote ( "'" ), "" ) + "',";
            } else {
                sql += nome + " = '" + valor.replaceAll ( Pattern.quote ( "'" ), "" ) + "'";
            }
        }
        sql += "where " + where;
        return sql;
    }

    public static String Insert ( String tabela, Map<String, String> mapa ) {
        List<String> values = new List<String> ();
        for ( Object key : mapa.getKeys () ) {
            values.add ( mapa.toString ( key ) );
        }
        return Insert ( tabela, mapa.getKeys ().toArray (), values.toStringArray () );
    }

    /**
     * Retorna o texto de um insert na tabela
     *
     * @param tabela nome da tabela
     * @param nomes nomes dos campos
     * @param valores valores dos campos
     * @return Texto com a instrução sql
     */
    public static String Insert ( String tabela, Object[] nomes, Object[] valores ) {
        String sql = "insert into " + tabela + " (";
        for ( int i = 0; i < nomes.length; i ++ ) {
            String valor = valores[ i ].toString ();
            String nome = nomes[ i ].toString ();
            if ( i < nomes.length - 1 ) {
                sql += nome + ",";
            } else {
                sql += nome;
            }
        }
        sql += ") values(";
        for ( int i = 0; i < nomes.length; i ++ ) {
            String valor = valores[ i ].toString ();
            String nome = nomes[ i ].toString ();
            if ( i < nomes.length - 1 ) {
                sql += "'" + valor.replaceAll ( Pattern.quote ( "'" ), "" ) + "',";//para não dizer que não falei das flores
            } else {
                sql += "'" + valor.replaceAll ( Pattern.quote ( "'" ), "" ) + "'";
            }
        }
        return sql + ")";
    }

    /**
     * Lê um arquivo e retorna a representação hexadecimal que é usado na
     * armazenagem dos tipos blobs
     *
     * @param file Arquivo a ser lido
     * @return Representação em hexadecimal
     */
    public static String fileToSQL ( File file ) {
        return Hex.encodeHexString ( UtilFile.loadBIN ( file ) );
    }

    /**
     * Transforma um hexadecima em um array de bytes para gravar com a classe
     * UtilFile exemplo UtilFile.writeBIN("a.jpg", Db.sQLToFile("aa a1 00"));
     *
     * @param input
     * @return Array de bytes
     */
    public static byte[] sQLToFile ( String input ) {
        byte[] retorno = null;
        try {
            retorno = Hex.decodeHex ( input.toCharArray () );
        } catch ( Exception e ) {
            Log.out ( Util.exeptionToString ( e ) );
        }
        return retorno;
    }

    public boolean exists ( String table, String where ) {
        return get ( "select id from " + table + " where " + where ).size () > 0;
    }
}
