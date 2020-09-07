package com.chessEngine.Piece;

import com.chessEngine.Alliance;
import com.chessEngine.board.Board;
import com.chessEngine.board.BoardUtils;
import com.chessEngine.board.Move;
import com.chessEngine.board.Move.*;
import com.chessEngine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {
    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINTE = {-9, -8, -7, -1, 1, 7, 8, 9};

    Queen(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList();
        int candidateDestinationCoordinate;
        for(int candidateCoodinateOffset : CANDIDATE_MOVE_VECTOR_COORDINTE){
            candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if (isFirstColumnExclusion(candidateCoodinateOffset, candidateDestinationCoordinate) ||
                        isEighthColumnExclusion(candidateCoodinateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += candidateCoodinateOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    else{
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getAlliance();
                        if(this.pieceAlliance != pieceAlliance){
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }

            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentCandidate,
                                                  final int candidateDestinationCoordinate) {
        return BoardUtils.FIRST_COLUMN[candidateDestinationCoordinate] && (currentCandidate == -1 || currentCandidate == -9 || currentCandidate == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int candidateDestinationCoordinate) {
        return BoardUtils.EIGHTH_COLUMN[candidateDestinationCoordinate] && (currentCandidate == 1 || currentCandidate == -7 || currentCandidate == 9);
    }

}