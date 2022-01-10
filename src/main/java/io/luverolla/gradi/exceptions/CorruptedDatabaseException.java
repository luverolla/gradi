package io.luverolla.gradi.exceptions;

public class CorruptedDatabaseException extends RuntimeException
{
    public CorruptedDatabaseException()
    {
        super("Some system data cannot be found. Either the database script hasn't been run or datasource is not correct");
    }
}
