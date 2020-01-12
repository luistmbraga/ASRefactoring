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
            this.SQLConn.connect();
            ResultSet rs = this.SQLConn.executeQuery("select * from Utilizador where" +
                    " Nome='"+username+"' and Password='" + password + "'");
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

        this.SQLConn.connect();

        this.SQLConn.executeUpdate("Update Utilizador set Saldo =Saldo +" + value + " where Nome='" + user.getUsername() + "'");

        SQLConn.disconnect();

    }

    @Override
    public void removeMoney(Utilizador user, double value) {

        this.SQLConn.connect();

        this.SQLConn.executeUpdate("Update Utilizador set  Saldo = if(Saldo>= +" + value + ",Saldo-"+ value + ",Saldo) where Nome='" + user.getUsername() + "'");

        SQLConn.disconnect();

    }


    @Override
    public String put(Utilizador obj) {
        try{
            this.SQLConn.connect();

            ResultSet rs = this.SQLConn.executeQuery("select * from Utilizador where Nome='"+obj.getUsername()+"'");
            if(!rs.next()){
                String cmd = "insert into Utilizador (Nome,Password,Saldo) values('" + obj.getUsername() + "','" + obj.getPassword() +"','"+ obj.getMoney() +"')";
                this.SQLConn.executeUpdate(cmd);
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
            this.SQLConn.connect();
            ResultSet rs1 = this.SQLConn.executeQuery("select * from AtivosPreferidos inner join AtivoFinanceiro on AtivosPreferidos.AtivoFinanceiro=AtivoFinanceiro.Nome where Utilizador='"+utilizador.getUsername()+"'");
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
            this.SQLConn.connect();

            ResultSet rs = this.SQLConn.executeQuery("select * from Utilizador where Nome='"+id+"'");
            if (rs.next()) {
                u = new Utilizador(rs.getString("Nome"), rs.getString("Password"), rs.getDouble("Saldo"));

            }

            ResultSet rs1 = this.SQLConn.executeQuery("select * from AtivosPreferidos inner join AtivoFinanceiro on AtivosPreferidos.AtivoFinanceiro=AtivoFinanceiro.Nome where Utilizador='"+u.getUsername()+"'");
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

        this.SQLConn.connect();

        this.SQLConn.executeUpdate("delete from Utilizador where Nome='" + id +"'");

        SQLConn.disconnect();
    }

    @Override
    public void replace(String id, Utilizador obj) {
        obj.setUsername(id);
        put(obj);
    }


    private void addPreferido( Utilizador u,AtivoFinanceiroFavorito a){

        this.SQLConn.connect();
        String cmd = "insert into AtivosPreferidos (AtivoFinanceiro,Utilizador, Valor) " +
                "values('" + a.getCompany() + "','"
                + u.getUsername() +"'," + a.getValueToNotify() + ")";
        this.SQLConn.executeUpdate(cmd);

        SQLConn.disconnect();

    }

    private void removePreferido(Utilizador u, AtivoFinanceiro a){
        try{
            this.SQLConn.connect();
            String cmd = "delete from AtivosPreferidos where Utilizador ='" + u.getUsername() + "' and AtivoFinanceiro='" + a.getCompany() + "'";
            System.out.println(cmd);
            this.SQLConn.executeUpdate(cmd);
        }
        catch (Exception e){e.printStackTrace();}
        finally {
            SQLConn.disconnect();
        }
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