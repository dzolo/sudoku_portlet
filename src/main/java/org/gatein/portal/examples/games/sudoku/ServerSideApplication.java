/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gatein.portal.examples.games.sudoku;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.gatein.portal.examples.games.sudoku.service.*;

/**
 *
 * @author dzolo
 */
public class ServerSideApplication extends Application
{
    private Set<Object> singletons = new HashSet<Object>();
 
	public ServerSideApplication()
    {
		singletons.add(new GameRestFacade());
		singletons.add(new GameSolutionRestFacade());
        singletons.add(new SavedGameSolutionRestFacade());
        singletons.add(new LastPlayedGameSolutionRestFacade());
        singletons.add(new ServicesRestFacade());
	}
 
	@Override
	public Set<Object> getSingletons()
    {
		return singletons;
	}
}
