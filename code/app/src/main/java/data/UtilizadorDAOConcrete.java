package data;

import business.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class UtilizadorDAOConcrete implements UtilizadorDAO {
    DBConnection SQLConn = new SQLConnection();

    @Override
    public boolean login(String username, String password) {
        boolean ret=false;
        try{
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("select * from Utilizador where Nome='"+username+"' and Password='" + password + "'");
            if(rs.next()){
                ret=true;
            }
        }
        catch (SQLException e){e.printStackTrace();}
        finally {
            SQLConn.disconnect();

        }

        return ret;
    }

    @Override
    public void addMoney(Utilizador user, double value) {
        try {
            Statement stmt = getStatement();

            stmt.executeUpdate("Update Utilizador set Saldo =Saldo +" + value + " where Nome='" + user.getUsername() + "'");


        }
        catch (SQLException e){e.printStackTrace();}
        finally {
            SQLConn.disconnect();
        }
    }

    private Statement getStatement() throws SQLException {
        SQLConn.connect();
        Connection conn = SQLConn.getConn();
        return conn.createStatement();
    }

    @Override
    public void removeMoney(Utilizador user, double value) {
        try {
            Statement stmt = getStatement();

            stmt.executeUpdate("Update Utilizador set  Saldo = if(Saldo>= +" + value + ",Saldo-"+ value + ",Saldo) where Nome='" + user.getUsername() + "'");


        }
        catch (SQLException e){e.printStackTrace();}
        finally {
            SQLConn.disconnect();
        }
    }


    @Override
    public String put(Utilizador obj) {
        try{
            Statement stmt = getStatement();

            ResultSet rs = stmt.executeQuery("select * from Utilizador where Nome='"+obj.getUsername()+"'");
            if(!rs.next()){
                String cmd = "insert into Utilizador (Nome,Password,Saldo) values('" + obj.getUsername() + "','" + obj.getPassword() +"','"+ obj.getMoney() +"')";
                stmt.executeUpdate(cmd);
            }
        }
        catch (SQLException e){e.printStackTrace();}
        finally {
            SQLConn.disconnect();
            putFavoritos(obj);
        }
        return obj.getUsername();
    }

    private void putFavoritos(Utilizador utilizador){
        try{
            Statement stmt = getStatement();
            ResultSet rs1 = stmt.executeQuery("select * from AtivosPreferidos inner join AtivoFinanceiro on AtivosPreferidos.AtivoFinanceiro=AtivoFinanceiro.Nome where Utilizador='"+utilizador.getUsername()+"'");
            List<AtivoFinanceiro> ativoFinanceiros = new LinkedList<>();
            while(rs1.next()){
                AtivoFinanceiro ativoFinanceiro = new AtivoFinanceiro(rs1.getString("Nome"),rs1.getDouble("ValorUnit"),rs1.getString("Type")) {};
                ativoFinanceiros.add(ativoFinanceiro);
            }
            for(AtivoFinanceiroFavorito ativoFinanceiro : utilizador.getFavoritos()){
                if(!ativoFinanceiros.contains(ativoFinanceiro)){
                    addPreferido(utilizador, ativoFinanceiro);
                }
            }
            for(AtivoFinanceiro ativoFinanceiro: ativoFinanceiros){
                if(!utilizador.getFavoritos().contains(ativoFinanceiro)){
                    removePreferido(utilizador, ativoFinanceiro);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            SQLConn.disconnect();
        }
    }


    @Override
    public Utilizador get(String id) {
        Utilizador u = null;
        AtivoFinanceiro a;
        try{
            SQLConn.connect();
            Connection conn = SQLConn.getConn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Utilizador where Nome='"+id+"'");
            if (rs.next()) {
                u = new Utilizador(rs.getString("Nome"), rs.getString("Password"), rs.getDouble("Saldo"));

            }

            Statement stmt2 = conn.createStatement();
            ResultSet rs1 = stmt2.executeQuery("select * from AtivosPreferidos inner join AtivoFinanceiro on AtivosPreferidos.AtivoFinanceiro=AtivoFinanceiro.Nome where Utilizador='"+u.getUsername()+"'");
            while(rs1.next()){
                double value = rs1.getDouble("Valor");
                a = new AtivoFinanceiro(rs1.getString("Nome"),rs1.getDouble("ValorUnit"),rs1.getString("Type")) {};
                u.addFavorito(a, value);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();}
        finally {
            SQLConn.disconnect();
        }
        return u;
    }

    @Override
    public void delete(String id) {
        try{
            Statement stmt = getStatement();

            stmt.executeUpdate("delete from Utilizador where Nome='" + id +"'");
        }
        catch (SQLException e){e.printStackTrace();}
        finally {
            SQLConn.disconnect();
        }

    }

    @Override
    public void replace(String id, Utilizador obj) {
        obj.setUsername(id);
        put(obj);
    }


    private void addPreferido( Utilizador u,AtivoFinanceiroFavorito a){
        try{
            Statement stmt = getStatement();
            String cmd = "insert into AtivosPreferidos (AtivoFinanceiro,Utilizador, Valor) values('" + a.getCompany() + "','"
                    + u.getUsername() +"'," + a.getValueToNotify() + ")";
            stmt.executeUpdate(cmd);
        }
        catch (SQLException e){e.printStackTrace();}
        finally {
            SQLConn.disconnect();
        }
    }

    private void removePreferido(Utilizador u, AtivoFinanceiro a){
        try{
            Statement stmt = getStatement();
            String cmd = "delete from AtivosPreferidos where Utilizador ='" + u.getUsername() + "' and AtivoFinanceiro='" + a.getCompany() + "'";
            System.out.println(cmd);
            stmt.executeUpdate(cmd);
        }
        catch (SQLException e){e.printStackTrace();}
        finally {
            SQLConn.disconnect();
        }
    }


    public void setValorPref(Utilizador u,AtivoFinanceiro a,double val){
        try{
            Statement stmt = getStatement();
            String cmd = "update  AtivosPreferidos set Valor=" + val  +"where Utilizador='" + u.getUsername()+"' and AtivoFinanceiro='" + a.getCompany() +"'";
            stmt.executeUpdate(cmd);

            SQLConn.disconnect();
        }
        catch (SQLException e){e.printStackTrace();}
    }

    public static void main(String[] args) {
        UtilizadorDAOConcrete uc = new UtilizadorDAOConcrete();
        AtivoFincanceiroDAOConcrete ac = new AtivoFincanceiroDAOConcrete();
        Petroleo p = new Petroleo("pet",20.0);
        Utilizador u = new Utilizador("fabio","111",10000.0);
        u.addFavorito(p, 10);

        ac.put(p);

        uc.put(u);

        Utilizador utilizador = uc.get("fabio");
        System.out.println(utilizador);

    }

}