package tracking.hibernate;

import org.hibernate.dialect.Oracle12cDialect;

public class CnxsOracleDialect extends Oracle12cDialect{

    public CnxsOracleDialect(){

        // The following keywords are necessary for correct processing of
        // the AgreementClin.getClinDescription @Formula. If they are not
        // provided, the Hibernate org.hibernate.sql.Template class incorrectly
        // attempts to prefix this keywords with the Table alias.
		registerKeyword("offset");
		registerKeyword("rows");
		registerKeyword("fetch");
		registerKeyword("first");
		registerKeyword("rows");
		registerKeyword("only");
    }
}
