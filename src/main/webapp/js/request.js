/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : request.js
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

/**
 * The Request class
 * 
 * @param contextPath   A context path of the whole application
 * @return SudokuGame_Request
 */
var SudokuGame_Request = (function (contextPath)
{
    /** The context path (private property) */
    var _contextPath = null;
    
    /**
     * Sets a context path of the Request class
     * 
     * @param contextPath   A context path of the whole application
     */
    this.setContextPath = function (contextPath)
    {
        if (!contextPath || !contextPath.length)
        {
            throw new SudokuGame_NullPointerException("An empty context path");
        }
        
        _contextPath = contextPath;
    }
    
    /**
     * Gets a context path of the Request class
     * 
     * @return              A content path
     */
    this.getContextPath = function ()
    {
        if (!_contextPath || !_contextPath.length)
        {
            throw new SudokuGame_NullPointerException("An empty context path");
        }
        
        return _contextPath;
    }

    /**
     * Gets context path of the Request class
     * 
     * @return              A path for REST requests
     */
    this.getPathOfRestApp = function ()
    {
        var contextPathNoLastBackslash = this.getContextPath();
        var lastLetter = contextPathNoLastBackslash.length - 1;

        if (contextPathNoLastBackslash.substr(lastLetter) == '/')
        {
            contextPathNoLastBackslash = contextPathNoLastBackslash.substr(0, lastLetter);
        }

        return contextPathNoLastBackslash + "/rest";
    }
    
    /**
     * Makes an GET request to a given path.
     * The path does not contains the context path of the application. 
     *
     * @example request.makeGet("/service/")
     * @param path          A path of a request
     * @return              A returned data object
     * @throws SudokuGame_RequestFailedException
     */
    this.makeGet = function (path)
    {
        var returnObj = null;
        
        $.ajax({
            url         : this.getPathOfRestApp() + path,
            async       : false,
            dataType    : 'json',
            type        : 'GET',
            success     : function (data, textStatus, jqXHR)
            {
                if (jqXHR.status != 200)
                {
                    throw new SudokuGame_RequestFailedException(textStatus);
                }
                
                returnObj = data;
            },
            error       : function (jqXHR, textStatus, et)
            {
                throw new SudokuGame_RequestFailedException(textStatus, et);
            }
        });
        
        return returnObj;
    }
    
    /**
     * Makes an POST request to a given path with JSON data.
     * The path does not contains the context path of the application. 
     *
     * @example request.makePost("/service/", {name: Ondrej, surname: Fibich})
     * @param path          A path of a request
     * @param data          An input data object of a request
     * @param async         An indicator of async [optional]
     * @return              A returned data object
     * @throws SudokuGame_RequestFailedException
     */
    this.makePost = function (path, data, async)
    {
        var returnObj = null;
        
        $.ajax({
            url         : this.getPathOfRestApp() + path,
            async       : (async != undefined && async === true),
            data        : JSON.stringify(data),
            type        : 'POST',
            dataType    : 'json',
            contentType : 'application/json',
            success     : function (data, textStatus, jqXHR)
            {
                if (jqXHR.status != 201)
                {
                    throw new SudokuGame_RequestFailedException(textStatus);
                }
                
                if (data)
                {
                    returnObj = data;
                }
                else
                {
                    returnObj = {location: jqXHR.getResponseHeader('location')};
                }
            },
            error       : function (jqXHR, textStatus, et)
            {
                throw new SudokuGame_RequestFailedException(
                        textStatus + ' ' + jqXHR.status, et
                );
            }
        });
        
        return returnObj;
    }
    
    /**
     * Makes an POST request to a given path with text/plain data.
     * The path does not contains the context path of the application. 
     *
     * @example request.makePostText("/service/", "data")
     * @param path          A path of a request
     * @param data          An input data string of a request
     * @return              A returned data string
     * @throws SudokuGame_RequestFailedException
     */
    this.makePostText = function (path, data)
    {
        var returnObj = null;
        
        $.ajax({
            url         : this.getPathOfRestApp() + path,
            async       : false,
            data        : data,
            type        : 'POST',
            dataType    : 'json',
            contentType : 'text/plain',
            success     : function (data, textStatus, jqXHR)
            {
                if (jqXHR.status != 201)
                {
                    throw new SudokuGame_RequestFailedException(textStatus);
                }
                
                returnObj = data;
            },
            error       : function (jqXHR, textStatus, et)
            {
                throw new SudokuGame_RequestFailedException(
                        textStatus + ' ' + jqXHR.status, et
                );
            }
        });
        
        return returnObj;
    }
    
    /**
     * Makes an PUT request to a given path.
     * The path does not contains the context path of the application. 
     *
     * @example request.makePut("/service/", {name: Ondrej, surname: Fibich})
     * @param path          A path of a request
     * @param data          An input data object of a request
     * @return              A returned data object (typically an ID of created element)
     * @throws SudokuGame_RequestFailedException
     */
    this.makePut = function (path, data)
    {
        var returnObj = null;
        
        $.ajax({
            url         : this.getPathOfRestApp() + path,
            async       : false,
            data        : JSON.stringify(data),
            type        : 'PUT',
            dataType    : 'json',
            contentType : 'application/json',
            success     : function (data, textStatus, jqXHR)
            {
                if (jqXHR.status != 200)
                {
                    throw new SudokuGame_RequestFailedException(textStatus);
                }
                
                return data;
            },
            error       : function (jqXHR, textStatus, et)
            {
                throw new SudokuGame_RequestFailedException(textStatus, et);
            }
        });
        
        return returnObj;
    }
    
    // CONSTRUCT START /////////////////////////////////////////////////////////
    this.setContextPath(contextPath);
    // CONTRUCT END    /////////////////////////////////////////////////////////
    
    // returns an instance of the class
    return this;
});
