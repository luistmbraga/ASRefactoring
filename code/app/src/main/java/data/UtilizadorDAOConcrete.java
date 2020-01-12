package data;

import business.*;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class UtilizadorDAOConcrete implements UtilizadorDAO {
    private DBConnection conn = new SQLConnection();

    @Override
    public boolean login(String username, String password) {
        boolean ret=false;
        try{
            this.conn.connect();
            ResultSet rs = this.conn.executeQuery("select * from Utilizador where" +
                    " Nome='"+username+"' and Password='" + password + "'");
            if(rs.next()){
                ret=true;
            }
        }
        catch (Exception e){e.printStackTrace();}
        finally {
            conn.disconnect();
        }

        return ret;
    }

    @Override
    public void addMoney(Utilizador user, double value) {

        this.conn.connect();

        try {
            this.conn.executeUpdate("Update Utilizador set Saldo =Saldo +" + value + " where Nome='" + user.getUsername() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
    }

    @Override
    public void removeMoney(Utilizador user, double value) {

        this.conn.connect();

        try {
            this.conn.executeUpdate("Update Utilizador set  Saldo = if(Saldo>= +" + value + ",Saldo-"+ value + ",Saldo) where Nome='" + user.getUsername() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
    }


    @Override
    public String put(Utilizador obj) {
        try{
            this.conn.connect();

            ResultSet rs = this.conn.executeQuery("select * from Utilizador where Nome='"+obj.getUsername()+"'");
            if(!rs.next()){
                String cmd = "insert into Utilizador (Nome,Password,Saldo) values('" + obj.getUsername() + "','" + obj.getPassword() +"','"+ obj.getMoney() +"')";
                this.conn.executeUpdate(cmd);
            }
        }
        catch (Exception e){e.printStackTrace();}
        finally {
            conn.disconnect();
            putFavoritos(obj);
        }
        return obj.getUsername();
    }

    private void putFavoritos(Utilizador utilizador){
        try{
            this.conn.connect();
            ResultSet rs1 = this.conn.executeQuery("select * from AtivosPreferidos inner join AtivoFinanceiro on AtivosPreferidos.AtivoFinanceiro=AtivoFinanceiro.Nome where Utilizador='"+utilizador.getUsername()+"'");
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
            conn.disconnect();
        }
    }


    @Override
    public Utilizador get(String id) {
        Utilizador u = null;
        AtivoFinanceiro a;
        try{
            this.conn.connect();
            ResultSet rs = this.conn.executeQuery("select * from Utilizador where Nome='"+id+"'");
            if (rs.next()) {
                u = new Utilizador(rs.getString("Nome"), rs.getString("Password"), rs.getDouble("Saldo"));

                ResultSet rs1 = this.conn.executeQuery("select * from AtivosPreferidos inner join AtivoFinanceiro " +
                        "on AtivosPreferidos.AtivoFinanceiro=AtivoFinanceiro.Nome " +
                        "where Utilizador='" + u.getUsername() + "'");
                while (rs1.next()) {
                    double value = rs1.getDouble("Valor");
                    a = new AtivoFinanceiro(rs1.getString("Nome"), rs1.getDouble("ValorUnit"), rs1.getString("Type")) {
                    };
                    u.addFavorito(a, value);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();}
        finally {
            conn.disconnect();
        }
        return u;
    }

    @Override
    public void delete(String id) {

        this.conn.connect();

        try {
            this.conn.executeUpdate("delete from Utilizador where Nome='" + id +"'");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
    }

    @Override
    public void replace(String id, Utilizador obj) {
        obj.setUsername(id);
        put(obj);
    }


    private void addPreferido( Utilizador u,AtivoFinanceiroFavorito a){

        this.conn.connect();
        String cmd = "insert into AtivosPreferidos (AtivoFinanceiro,Utilizador, Valor) " +
                "values('" + a.getCompany() + "','"
                + u.getUsername() +"'," + a.getValueToNotify() + ")";
        try {
            this.conn.executeUpdate(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
    }

    private void removePreferido(Utilizador u, AtivoFinanceiro a){
        try{
            this.conn.connect();
            String cmd = "delete from AtivosPreferidos where Utilizador ='" + u.getUsername() + "' and AtivoFinanceiro='" + a.getCompany() + "'";
            System.out.println(cmd);
            this.conn.executeUpdate(cmd);
        }
        catch (Exception e){e.printStackTrace();}
        finally {
            conn.disconnect();
        }
    }

}