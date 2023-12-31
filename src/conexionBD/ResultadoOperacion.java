package conexionBD;

public class ResultadoOperacion {
    private boolean error;
    private String message;
    private int numberAffectedRows;

    public ResultadoOperacion() {
    }

    public ResultadoOperacion(boolean error, String message, int numberAffectedRows) {
        this.error = error;
        this.message = message;
        this.numberAffectedRows = numberAffectedRows;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNumberRowsAffected() {
        return numberAffectedRows;
    }

    public void setNumberRowsAffected(int numberAffectedRows) {
        this.numberAffectedRows = numberAffectedRows;
    }  
}
